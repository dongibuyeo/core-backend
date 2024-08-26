package com.shinhan.dongibuyeo.domain.challenge.entity;

import com.github.f4b6a3.ulid.UlidCreator;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyScore {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id = UlidCreator.getMonotonicUlid().toUuid();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_challenge_id")
    private MemberChallenge memberChallenge;

    private LocalDate date;
    private int totalScore;

    @Column(columnDefinition = "JSON")
    private String scoreDetails;  // JSON 형식으로 점수 세부 정보 저장

    @Builder
    public DailyScore(LocalDate date, String scoreDetails, int totalScore) {
        this.date = date;
        this.scoreDetails = scoreDetails;
        this.totalScore = totalScore;
    }

    public void updateMemberChallenge(MemberChallenge memberChallenge) {
        this.memberChallenge = memberChallenge;
        if (memberChallenge != null && !memberChallenge.getDailyScores().contains(this)) {
            memberChallenge.getDailyScores().add(this);
        }
    }

    public void updateDailyTotalScore(int score, String item) {
        this.totalScore += score;
    }

    public void updateScoreDetails(String scoreDetails) {
        this.scoreDetails = scoreDetails;
    }
}