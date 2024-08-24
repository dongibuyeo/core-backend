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

    private Long reward;

    private Long totalPoints;

    @Builder
    public MemberChallenge(Member member, Challenge challenge, Long deposit) {
        this.member = member;
        this.challenge = challenge;
        this.isSuccess = false;
        this.deposit = deposit;
        this.reward = 0L;
        this.totalPoints = 0L;
    }

    public void addDailyScore(DailyScore dailyScore) {
        this.dailyScores.add(dailyScore);
        this.totalPoints += dailyScore.getTotalScore();
    }

    public void determineSuccess() {
        // 성공 여부를 결정하는 로직 구현
        // 예: this.isSuccess = this.totalPoints >= challenge.getSuccessThreshold();
    }

    public void calculateReward() {
        // 보상을 계산하는 로직 구현
        // 예: this.reward = isSuccess ? deposit + challenge.getAdditionalReward() : 0;
    }
}