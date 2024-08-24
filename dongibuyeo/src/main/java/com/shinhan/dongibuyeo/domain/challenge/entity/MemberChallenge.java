package com.shinhan.dongibuyeo.domain.challenge.entity;

import com.github.f4b6a3.ulid.UlidCreator;
import com.shinhan.dongibuyeo.domain.member.entity.Member;
import com.shinhan.dongibuyeo.domain.score.entity.DailyScore;
import com.shinhan.dongibuyeo.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
public class MemberChallenge extends BaseEntity {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id = UlidCreator.getMonotonicUlid().toUuid();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @OneToMany(mappedBy = "memberChallenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyScore> dailyScores = new ArrayList<>();

    private Boolean isSuccess;

    private Long deposit;

    private Long baseReward;

    private Long additionalReward;

    private Long totalPoints;

    @Builder
    public MemberChallenge(Member member, Challenge challenge, Long deposit) {
        this.member = member;
        this.challenge = challenge;
        this.isSuccess = false;
        this.deposit = deposit;
        this.baseReward = 0L;
        this.additionalReward = 0L;
        this.totalPoints = 0L;
    }

    public void addDailyScore(DailyScore dailyScore) {
        this.dailyScores.add(dailyScore);
        dailyScore.updateMemberChallenge(this);
        this.totalPoints += dailyScore.getTotalScore();
    }

    public void updateSuccessStatus(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public void updateBaseReward(Long baseReward) {
        this.baseReward = baseReward;
    }

    public void updateAdditionalReward(Long additionalReward) {
        this.additionalReward = additionalReward;
    }

}