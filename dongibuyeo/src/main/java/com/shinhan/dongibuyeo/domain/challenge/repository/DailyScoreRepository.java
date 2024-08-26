package com.shinhan.dongibuyeo.domain.challenge.repository;

import com.shinhan.dongibuyeo.domain.challenge.entity.DailyScore;
import com.shinhan.dongibuyeo.domain.challenge.entity.MemberChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface DailyScoreRepository extends JpaRepository<DailyScore, UUID> {

    Optional<DailyScore> findByMemberChallengeAndDate(MemberChallenge memberChallenge, LocalDate date);
}
