package com.shinhan.dongibuyeo.domain.score.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinhan.dongibuyeo.domain.account.dto.request.TransactionHistoryRequest;
import com.shinhan.dongibuyeo.domain.account.dto.response.TransactionHistory;
import com.shinhan.dongibuyeo.domain.account.service.AccountService;
import com.shinhan.dongibuyeo.domain.challenge.dto.response.ChallengeResponse;
import com.shinhan.dongibuyeo.domain.challenge.entity.Challenge;
import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeStatus;
import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeType;
import com.shinhan.dongibuyeo.domain.challenge.entity.MemberChallenge;
import com.shinhan.dongibuyeo.domain.challenge.service.ChallengeService;
import com.shinhan.dongibuyeo.domain.member.entity.Member;
import com.shinhan.dongibuyeo.domain.score.entity.DailyScore;
import com.shinhan.dongibuyeo.domain.score.exception.ScoreStrategyNotFoundException;
import com.shinhan.dongibuyeo.domain.score.strategy.ScoringStrategy;
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

    public ScoreCalculationService(AccountService accountService, Map<ChallengeType, ScoringStrategy> scoringStrategies, ChallengeService challengeService, ObjectMapper objectMapper) {
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
                throw new ScoreStrategyNotFoundException(challengeType);
            }

            Map<String, Integer> scores = strategy.calculateScore(transactions, date);
            DailyScore dailyScore = new DailyScore(date, objectMapper.writeValueAsString(scores));
            memberChallenge.addDailyScore(dailyScore);

            log.info("Calculated score for member: {}, challenge: {}, date: {}",
                    member.getId(), memberChallenge.getChallenge().getId(), date);
        } catch (JsonProcessingException e) {
            log.error("Error processing JSON for member challenge: {}", memberChallenge.getId(), e);
        } catch (ScoreStrategyNotFoundException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error("Error calculating score for member challenge: {}", memberChallenge.getId(), e);
        }
    }
}