package com.shinhan.dongibuyeo.domain.challenge.repository;

import com.shinhan.dongibuyeo.domain.challenge.entity.DailyScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface DailyScoreRepository extends JpaRepository<DailyScore, UUID> {

    @Query("SELECT  ds " +
            "FROM DailyScore ds " +
            "WHERE ds.memberChallenge.id = :memberChallengeId " +
            "AND ds.date = :date ")
    Optional<DailyScore> findByMemberChallengeIdAndDate(UUID memberChallengeId, LocalDate date);
}
