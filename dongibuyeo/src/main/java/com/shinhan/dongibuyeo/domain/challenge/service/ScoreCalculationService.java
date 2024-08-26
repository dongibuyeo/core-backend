package com.shinhan.dongibuyeo.domain.challenge.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinhan.dongibuyeo.domain.account.dto.request.TransactionHistoryRequest;
import com.shinhan.dongibuyeo.domain.account.dto.response.TransactionHistory;
import com.shinhan.dongibuyeo.domain.account.service.AccountService;
import com.shinhan.dongibuyeo.domain.challenge.dto.response.ChallengeResponse;
import com.shinhan.dongibuyeo.domain.challenge.dto.response.DailyScoreDetail;
import com.shinhan.dongibuyeo.domain.challenge.entity.*;
import com.shinhan.dongibuyeo.domain.challenge.exception.ChallengeScoringException;
import com.shinhan.dongibuyeo.domain.challenge.exception.ScoringParsingException;
import com.shinhan.dongibuyeo.domain.challenge.exception.ScoringStrategyNotFoundException;
import com.shinhan.dongibuyeo.domain.challenge.strategy.ScoringStrategy;
import com.shinhan.dongibuyeo.domain.member.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ScoreCalculationService {

    private final AccountService accountService;
    private final Map<ChallengeType, ScoringStrategy> scoringStrategies;
    private final ChallengeService challengeService;
    private final ObjectMapper objectMapper;

    public ScoreCalculationService(AccountService accountService,
                                   Map<ChallengeType, ScoringStrategy> scoringStrategies,
                                   ChallengeService challengeService,
                                   ObjectMapper objectMapper) {
        this.accountService = accountService;
        this.scoringStrategies = scoringStrategies;
        this.challengeService = challengeService;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void calculateDailyScores(LocalDate date) {
        List<ChallengeResponse> activeChallenges = challengeService.findAllChallengesByStatus(ChallengeStatus.IN_PROGRESS);

        for (ChallengeResponse challengeResponse : activeChallenges) {
            try {
                challengeService.findChallengeById(challengeResponse.getChallengeId())
                        .getChallengeMembers()
                        .forEach(memberChallenge -> calculateScoreForMemberChallenge(memberChallenge, date));

            } catch (Exception e) {
                log.error("Error calculating scores for challenge: {}", challengeResponse.getChallengeId(), e);
            }
        }
    }

    private void calculateScoreForMemberChallenge(MemberChallenge memberChallenge, LocalDate date) {
        try {
            // 거래내역 조회
            Member member = memberChallenge.getMember();
            String accountNumber = member.getChallengeAccount().getAccountNo();

            TransactionHistoryRequest request = TransactionHistoryRequest.builder()
                    .memberId(member.getId())
                    .accountNo(accountNumber)
                    .startDate(date.toString())
                    .endDate(date.toString())
                    .transactionType("D")
                    .orderByType("ASC")
                    .build();

            List<TransactionHistory> transactions = accountService.getMemberTransactionHistory(request).getTransactions();

            // 챌린지 정산 전략 조회
            ChallengeType challengeType = memberChallenge.getChallenge().getType();
            ScoringStrategy strategy = scoringStrategies.get(challengeType);
            if (strategy == null) {
                throw new ScoringStrategyNotFoundException(challengeType);
            }

            Map<String, Integer> scores = strategy.calculateScore(transactions, date);
            int dailyTotalScore = calculateTotalScore(scores);
            List<ScoreDetail> scoreEntries = createScoreEntries(scores, memberChallenge.getTotalScore());
            String scoreDetailsJson = objectMapper.writeValueAsString(scoreEntries);

            DailyScore dailyScore = new DailyScore(date);
            memberChallenge.addDailyScore(dailyScore);

        } catch (Exception e) {
            log.error("Error calculating score for member challenge: {}", memberChallenge.getId(), e);
        }
    }

    public int calculateTotalScore(Map<String, Integer> scores) {
        return scores.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    private List<ScoreDetail> createScoreEntries(Map<String, Integer> scores, int initialTotalScore) {
        List<ScoreDetail> entries = new ArrayList<>();
        int runningTotal = initialTotalScore;
        for (Map.Entry<String, Integer> score : scores.entrySet()) {
            runningTotal += score.getValue();
            entries.add(new ScoreDetail(score.getKey(), score.getValue(), runningTotal));
        }
        return entries;
    }

    public DailyScoreDetail convertToDailyScoreDetail(DailyScore dailyScore) {
        try {
            List<ScoreDetail> scoreEntries = parseScoreEntries(dailyScore);
            return new DailyScoreDetail(
                    dailyScore.getDate().toString(),
                    scoreEntries
            );
        } catch (Exception e) {
            log.error("Error converting daily score to detail for score ID: {}", dailyScore.getId(), e);
            throw new ChallengeScoringException(dailyScore.getId());
        }
    }

    public List<ScoreDetail> parseScoreEntries(DailyScore dailyScore) {
        try {
            return objectMapper.readValue(dailyScore.getScoreDetails(), new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            log.error("Error parsing score entries for score ID: {}", dailyScore.getId(), e);
            throw new ScoringParsingException(dailyScore.getId());
        }
    }
}