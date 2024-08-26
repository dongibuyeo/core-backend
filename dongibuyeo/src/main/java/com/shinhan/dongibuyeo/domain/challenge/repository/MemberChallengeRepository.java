package com.shinhan.dongibuyeo.domain.challenge.repository;

import com.shinhan.dongibuyeo.domain.challenge.dto.response.MemberChallengeResponse;
import com.shinhan.dongibuyeo.domain.challenge.entity.Challenge;
import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeStatus;
import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeType;
import com.shinhan.dongibuyeo.domain.challenge.entity.MemberChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberChallengeRepository extends JpaRepository<MemberChallenge, UUID> {

    Optional<MemberChallenge> findMemberChallengeByChallengeIdAndMemberId(UUID challengeId, UUID memberId);

    @Query("SELECT c " +
            "FROM Challenge c " +
            "JOIN FETCH MemberChallenge mc " +
            "ON mc.challenge.id = c.id " +
            "WHERE mc.member.id = :memberId ")
    List<Challenge> findChallengesByMemberId(UUID memberId);

    @Query("SELECT c, mc.isSuccess, mc.deposit as memberDeposit, mc.baseReward, mc.additionalReward, mc.totalScore " +
            "FROM Challenge c " +
            "JOIN FETCH MemberChallenge mc " +
            "ON mc.challenge.id = c.id " +
            "WHERE mc.member.id = :memberId " +
            "AND mc.challenge.id = :challengeId")
    Optional<MemberChallengeResponse> findChallengeByMemberIdAndChallengeId(UUID memberId, UUID challengeId);

    @Query("SELECT mc " +
            "FROM MemberChallenge mc " +
            "JOIN FETCH Challenge c " +
            "ON mc.challenge.id = c.id " +
            "WHERE c.status = :status ")
    List<MemberChallenge> findAllByChallengeStatus(ChallengeStatus status);

    List<MemberChallenge> findAllByChallengeId(UUID challengeId);

    List<MemberChallenge> findByMemberId(UUID memberId);

    @Query("SELECT mc " +
            "FROM MemberChallenge mc " +
            "JOIN FETCH Challenge c " +
            "ON mc.challenge.id = c.id " +
            "WHERE mc.member.id = :memerId " +
            "AND c.type = :challengeType ")
    Optional<MemberChallenge> findByMemberIdAndChallengeType(UUID memberId, ChallengeType challengeType);
}
