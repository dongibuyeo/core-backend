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
                Challenge challenge = challengeService.findChallengeById(challengeResponse.getChallengeId());
                List<MemberChallenge> challengeMembers = challenge.getChallengeMembers();
                for (MemberChallenge memberChallenge : challengeMembers) {
                    calculateScoreForMemberChallenge(memberChallenge, date);
                }
            } catch (Exception e) {
                log.error("Error calculating scores for challenge: {}", challengeResponse.getChallengeId(), e);
            }
        }
    }

    private void calculateScoreForMemberChallenge(MemberChallenge memberChallenge, LocalDate date) {
        try {
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

            ChallengeType challengeType = memberChallenge.getChallenge().getType();
            ScoringStrategy strategy = scoringStrategies.get(challengeType);

            if (strategy == null) {
                throw new ScoringStrategyNotFoundException(challengeType);
            }

            Map<String, Integer> scores = strategy.calculateScore(transactions, date);
            int totalScore = calculateTotalScore(scores);
            DailyScore dailyScore = new DailyScore(date, objectMapper.writeValueAsString(scores));
            memberChallenge.addDailyScore(dailyScore, totalScore);

            log.info("Calculated score for member: {}, challenge: {}, date: {}, totalScore: {}",
                    member.getId(), memberChallenge.getChallenge().getId(), date, totalScore);
        } catch (JsonProcessingException e) {
            log.error("Error processing JSON for member challenge: {}", memberChallenge.getId(), e);
        } catch (ScoringStrategyNotFoundException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error("Error calculating score for member challenge: {}", memberChallenge.getId(), e);
        }
    }

    public int calculateTotalScore(MemberChallenge memberChallenge) {
        return memberChallenge.getDailyScores().stream()
                .mapToInt(this::calculateDailyScore)
                .sum();
    }

    public int calculateDailyScore(DailyScore dailyScore) {
        try {
            Map<String, Integer> scoreDetails = parseScoreDetails(dailyScore);
            return calculateTotalScore(scoreDetails);
        } catch (Exception e) {
            log.error("Error calculating daily score for score ID: {}", dailyScore.getId(), e);
            return 0;
        }
    }

    public Map<String, Integer> parseScoreDetails(DailyScore dailyScore) {
        try {
            return objectMapper.readValue(dailyScore.getScoreDetails(), new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            log.error("Error parsing score details for score ID: {}", dailyScore.getId(), e);
            throw new ScoringParsingException(dailyScore.getId());
        }
    }

    private int calculateTotalScore(Map<String, Integer> scoreDetails) {
        return scoreDetails.values().stream().mapToInt(Integer::intValue).sum();
    }

    public DailyScoreDetail convertToDailyScoreDetail(DailyScore dailyScore) {
        try {
            Map<String, Integer> scoreDetails = parseScoreDetails(dailyScore);
            int totalScore = calculateTotalScore(scoreDetails);
            return new DailyScoreDetail(
                    dailyScore.getDate().toString(),
                    totalScore,
                    scoreDetails
            );
        } catch (Exception e) {
            log.error("Error converting daily score to detail for score ID: {}", dailyScore.getId(), e);
            throw new ChallengeScoringException(dailyScore.getId());
        }
    }
}