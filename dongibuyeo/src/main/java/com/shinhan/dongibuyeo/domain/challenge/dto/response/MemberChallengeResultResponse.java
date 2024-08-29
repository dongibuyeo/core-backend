package com.shinhan.dongibuyeo.domain.challenge.dto.response;

import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeStatus;
import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeType;
import com.shinhan.dongibuyeo.domain.challenge.entity.MemberChallengeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class MemberChallengeResultResponse {

    private UUID memberId;
    private UUID challengeId;
    private ChallengeType type;
    private ChallengeStatus status;

    private String startDate;
    private String endDate;
    private String title;
    private String description;
    private String image;

    private MemberChallengeStatus myStatus;
    private boolean isSuccess;

    private long baseReward;
    private long additionalReward;
    private long beforeConsume;
    private long currentConsume;

    private long top10PercentRewardPerUnit; // 상위 10% 상금
    private long lower90PercentRewardPerUnit; // 하위 90% 상금

    private int top10PercentMemberNum;
    private int lower90PercentMemberNum;

    @Builder
    public MemberChallengeResultResponse(UUID memberId, UUID challengeId, ChallengeType type, ChallengeStatus status,
                                         LocalDate startDate, LocalDate endDate, String title, String description,
                                         String image, MemberChallengeStatus myStatus,
                                         boolean isSuccess, long baseReward, long additionalReward, long beforeConsume, long currentConsume,
                                         long top10PercentRewardPerUnit, long lower90PercentRewardPerUnit, int top10PercentMemberNum, int lower90PercentMemberNum) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        this.memberId = memberId;
        this.challengeId = challengeId;
        this.type = type;
        this.status = status;
        this.startDate = (startDate != null) ? startDate.format(formatter) : null;
        this.endDate = (endDate != null) ? endDate.format(formatter) : null;
        this.title = title;
        this.description = description;
        this.image = image;
        this.myStatus = myStatus;
        this.isSuccess = isSuccess;
        this.baseReward = baseReward;
        this.additionalReward = additionalReward;
        this.beforeConsume = beforeConsume;
        this.currentConsume = currentConsume;
        this.top10PercentRewardPerUnit = top10PercentRewardPerUnit;
        this.lower90PercentRewardPerUnit = lower90PercentRewardPerUnit;
        this.top10PercentMemberNum = top10PercentMemberNum;
        this.lower90PercentMemberNum = lower90PercentMemberNum;
    }

}
