package com.shinhan.dongibuyeo.domain.challenge.repository;

import com.shinhan.dongibuyeo.domain.challenge.dto.response.ChallengeResponse;
import com.shinhan.dongibuyeo.domain.challenge.entity.Challenge;
import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeStatus;
import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChallengeRepository extends JpaRepository<Challenge, UUID> {

    Optional<Challenge> findChallengeById(UUID challengeId);

    List<Challenge> findChallengesByEndDate(LocalDate endDate);

    @Query("SELECT c " +
            "FROM Challenge c " +
            "WHERE c.status = :status ")
    List<Challenge> findAllChallengesByStatus(ChallengeStatus status);

    @Query("SELECT c FROM Challenge c JOIN FETCH c.challengeMembers WHERE c.type = :type AND :curDay BETWEEN c.startDate AND c.endDate")
    List<Challenge> findAllChallengesByStatusAndDate(ChallengeType type, LocalDate curDay);
}
