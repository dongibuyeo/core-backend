package com.shinhan.dongibuyeo.domain.challenge.dto.response;

import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeStatus;
import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ChallengeResultResponse {

    private UUID challengeId;
    private ChallengeType type;
    private ChallengeStatus status;

    private String startDate;
    private String endDate;

    private String title;
    private String description;

    private Long totalDeposit;
    private Integer participants;

    private long totalReward; // 총 상금
    private long interestEarned; // 이자
    private long remainingFromFailures; // 잔여 예치금(실패 예치금)
    private long top10PercentRewardPerUnit; // 상위 10% 상금
    private long lower90PercentRewardPerUnit; // 하위 90% 상금

    private int top10PercentMemberNum;
    private int lower90PercentMemberNum;

    @Builder
    public ChallengeResultResponse(UUID challengeId, ChallengeType type, ChallengeStatus status,
                                   LocalDate startDate, LocalDate endDate, String title, String description, Long totalDeposit, Integer participants,
                                   long totalReward, long interestEarned, long remainingFromFailures, long top10PercentRewardPerUnit, long lower90PercentRewardPerUnit,
                                   int top10PercentMemberNum, int lower90PercentMemberNum) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        this.challengeId = challengeId;
        this.type = type;
        this.status = status;
        this.startDate = (startDate != null) ? startDate.format(formatter) : null;
        this.endDate = (endDate != null) ? endDate.format(formatter) : null;
        this.title = title;
        this.description = description;
        this.totalDeposit = totalDeposit;
        this.participants = participants;
        this.totalReward = totalReward;
        this.interestEarned = interestEarned;
        this.remainingFromFailures = remainingFromFailures;
        this.top10PercentRewardPerUnit = top10PercentRewardPerUnit;
        this.lower90PercentRewardPerUnit = lower90PercentRewardPerUnit;
        this.top10PercentMemberNum = top10PercentMemberNum;
        this.lower90PercentMemberNum = lower90PercentMemberNum;
    }

}
