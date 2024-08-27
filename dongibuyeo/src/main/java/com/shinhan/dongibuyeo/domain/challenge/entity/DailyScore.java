package com.shinhan.dongibuyeo.domain.challenge.entity;

import com.github.f4b6a3.ulid.UlidCreator;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.*;

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

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "score_entries", joinColumns = @JoinColumn(name = "daily_score_id"))
    private List<ScoreDetail> scoreDetails = new ArrayList<>();

    @Builder
    public DailyScore(LocalDate date) {
        this.date = date;
    }

    public void updateMemberChallenge(MemberChallenge memberChallenge) {
        this.memberChallenge = memberChallenge;
        if (memberChallenge != null && !memberChallenge.getDailyScores().contains(this)) {
            memberChallenge.getDailyScores().add(this);
        }
    }

    public void addScoreDetail(ScoreDetail scoreDetail) {
        this.scoreDetails.add(scoreDetail);
        int scoreDifference = scoreDetail.getScore();
        this.totalScore += scoreDifference;
        this.memberChallenge.addTotalScore(scoreDifference);
    }

    public List<ScoreDetail> getScoreDetails() {
        return Collections.unmodifiableList(scoreDetails);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyScore that = (DailyScore) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}