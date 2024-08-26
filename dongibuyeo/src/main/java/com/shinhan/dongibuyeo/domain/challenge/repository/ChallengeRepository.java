package com.shinhan.dongibuyeo.domain.challenge.repository;

import com.shinhan.dongibuyeo.domain.challenge.dto.response.ChallengeResponse;
import com.shinhan.dongibuyeo.domain.challenge.entity.Challenge;
import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChallengeRepository extends JpaRepository<Challenge, UUID> {

    Optional<Challenge> findChallengeById(UUID challengeId);

    List<Challenge> findChallengesByStatus(ChallengeStatus status);

    @Query("SELECT c " +
            "FROM Challenge c " +
            "JOIN FETCH c.challengeMembers mc " +
            "WHERE mc.member.id = :memberId AND c.status = :status")
    List<ChallengeResponse> findChallengesByMemberIdAndStatus(UUID memberId, ChallengeStatus status);


    List<Challenge> findChallengesByEndDate(LocalDate endDate);
}
