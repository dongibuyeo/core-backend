package com.shinhan.dongibuyeo.domain.challenge.service;

import com.shinhan.dongibuyeo.domain.account.dto.request.TransactionHistoryRequest;
import com.shinhan.dongibuyeo.domain.account.dto.response.TransactionHistory;
import com.shinhan.dongibuyeo.domain.challenge.entity.*;
import com.shinhan.dongibuyeo.domain.challenge.repository.DailyScoreRepository;
import com.shinhan.dongibuyeo.domain.challenge.score.scheduler.FeverTimeInfo;
import com.shinhan.dongibuyeo.domain.consume.dto.request.ConsumtionRequest;
import com.shinhan.dongibuyeo.domain.consume.service.ConsumeService;
import com.shinhan.dongibuyeo.domain.member.entity.Member;
import com.shinhan.dongibuyeo.global.entity.TransferType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class DailyScoreService {

    private final DailyScoreRepository dailyScoreRepository;
    private final MemberChallengeService memberChallengeService;
    private final ConsumeService consumeService;

    public DailyScoreService(DailyScoreRepository dailyScoreRepository, MemberChallengeService memberChallengeService, ConsumeService consumeService) {
        this.dailyScoreRepository = dailyScoreRepository;
        this.memberChallengeService = memberChallengeService;
        this.consumeService = consumeService;
    }

    @Transactional
    public DailyScore getOrCreateDailyScore(MemberChallenge memberChallenge, LocalDate date) {
        return dailyScoreRepository.findByMemberChallengeAndDate(memberChallenge, date)
                .orElseGet(() -> {
                    DailyScore newDailyScore = new DailyScore(date);
                    newDailyScore.updateMemberChallenge(memberChallenge);
                    newDailyScore.updateScoreDetails(new ScoreDetail("Daily Base Score", 10, 10));
                    memberChallenge.addDailyScore(newDailyScore);
                    return dailyScoreRepository.save(newDailyScore);
                });
    }

    @Transactional
    public void rewardNonConsumptionDuringFeverTime(ChallengeType challengeType, TransferType transferType) {
        List<MemberChallenge> activeChallenges = memberChallengeService.findsByChallengeTypeAndStatus(challengeType, ChallengeStatus.IN_PROGRESS);
        List<FeverTimeInfo> feverTimes = getFeverTimes(challengeType);

        for (FeverTimeInfo feverTime : feverTimes) {
            for (MemberChallenge challenge : activeChallenges) {
                if (!hasConsumptionDuring(challenge, feverTime.getStart(), feverTime.getEnd(), transferType)) {
                    DailyScore dailyScore = getOrCreateDailyScore(challenge, LocalDate.now());
                    String description = feverTime.getDescription();
                    int score = feverTime.getScore();
                    dailyScore.updateScoreDetails(new ScoreDetail(description, score, dailyScore.getTotalScore() + score));
                    dailyScoreRepository.save(dailyScore);
                }
            }
        }
    }

    private List<FeverTimeInfo> getFeverTimes(ChallengeType challengeType) {
        LocalDateTime now = LocalDateTime.now();
        return switch (challengeType) {
            case CONSUMPTION_COFFEE -> List.of(
                    new FeverTimeInfo(now.withHour(7).withMinute(0).withSecond(0),
                            now.withHour(10).withMinute(0).withSecond(0),
                            "[FEVER] 7AM-10AM", 2),
                    new FeverTimeInfo(now.withHour(11).withMinute(0).withSecond(0),
                            now.withHour(14).withMinute(0).withSecond(0),
                            "[FEVER] 11AM-2PM", 3)
            );
            case CONSUMPTION_DRINK -> {
                DayOfWeek today = now.getDayOfWeek();
                if (today == DayOfWeek.SATURDAY) {
                    yield List.of(
                            new FeverTimeInfo(now.minusDays(1).withHour(0).withMinute(0).withSecond(0),
                                    now.minusDays(1).withHour(23).withMinute(59).withSecond(59),
                                    "[FEVER] Friday", 5)
                    );
                } else if (today == DayOfWeek.SUNDAY) {
                    yield List.of(
                            new FeverTimeInfo(now.minusDays(1).withHour(0).withMinute(0).withSecond(0),
                                    now.minusDays(1).withHour(23).withMinute(59).withSecond(59),
                                    "[FEVER] Saturday", 5)
                    );
                } else {
                    yield List.of(); // 다른 요일에는 피버타임 없음
                }
            }
            case CONSUMPTION_DELIVERY -> List.of(
                    new FeverTimeInfo(now.minusDays(1).withHour(21).withMinute(0).withSecond(0),
                            now.withHour(2).withMinute(0).withSecond(0),
                            "[FEVER] 9PM-2AM", 5)
            );
            default -> throw new IllegalArgumentException("Invalid challenge type for fever time");
        };
    }

    private boolean hasConsumptionDuring(MemberChallenge challenge, LocalDateTime start, LocalDateTime end, TransferType transferType) {
        Member member = challenge.getMember();
        ConsumtionRequest request = new ConsumtionRequest(
                transferType,
                TransactionHistoryRequest.builder()
                        .memberId(member.getId())
                        .accountNo(member.getChallengeAccount().getAccountNo())
                        .startDate(start.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
                        .endDate(end.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
                        .build()
        );

        List<TransactionHistory> transactions = consumeService.getTypeHistory(request);

        return transactions.stream()
                .anyMatch(transaction -> {
                    LocalDateTime transactionDateTime = LocalDateTime.parse(
                            transaction.getTransactionDate() + transaction.getTransactionTime(),
                            DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
                    );
                    return !transactionDateTime.isBefore(start) && !transactionDateTime.isAfter(end);
                });
    }

    @Transactional
    public void updateDailyScore(MemberChallenge memberChallenge, LocalDate date, String description, int score) {
        DailyScore dailyScore = getOrCreateDailyScore(memberChallenge, date);
        int currentScore = dailyScore.getTotalScore();
        ScoreDetail scoreDetail = new ScoreDetail(description, score, currentScore + score);
        dailyScore.updateScoreDetails(scoreDetail);
    }
}
