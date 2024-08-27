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
@Builder
@AllArgsConstructor
public class MemberChallengeResponse {

    private UUID challengeId;
    private ChallengeType type;
    private ChallengeStatus status;
    private String accountNo;
    private String startDate;
    private String endDate;

    private String title;
    private String description;

    private Long totalDeposit;
    private Integer participants;

    private Boolean isSuccess;
    private Long memberDeposit;
    private Long baseReward;
    private Long additionalReward;
    private Integer totalScore;

    public MemberChallengeResponse(UUID challengeId, ChallengeType type, ChallengeStatus status,
                                   String accountNo, LocalDate startDate, LocalDate endDate,
                                   String title, String description, Long totalDeposit,
                                   Integer participants, Boolean isSuccess, Long memberDeposit,
                                   Long baseReward, Long additionalReward, Integer totalScore) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        this.challengeId = challengeId;
        this.type = type;
        this.status = status;
        this.accountNo = accountNo;
        this.startDate = (startDate != null) ? startDate.format(formatter) : null;
        this.endDate = (endDate != null) ? endDate.format(formatter) : null;
        this.title = title;
        this.description = description;
        this.totalDeposit = totalDeposit;
        this.participants = participants;
        this.isSuccess = isSuccess;
        this.memberDeposit = memberDeposit;
        this.baseReward = baseReward;
        this.additionalReward = additionalReward;
        this.totalScore = totalScore;
    }
}
