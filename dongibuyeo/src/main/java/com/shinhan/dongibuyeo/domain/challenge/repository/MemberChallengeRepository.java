package com.shinhan.dongibuyeo.domain.challenge.repository;

import com.shinhan.dongibuyeo.domain.challenge.dto.response.ChallengeRewardStatistics;
import com.shinhan.dongibuyeo.domain.challenge.dto.response.MemberChallengeResponse;
import com.shinhan.dongibuyeo.domain.challenge.dto.response.TopRankerInfo;
import com.shinhan.dongibuyeo.domain.challenge.entity.Challenge;
import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeStatus;
import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeType;
import com.shinhan.dongibuyeo.domain.challenge.entity.MemberChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("SELECT new com.shinhan.dongibuyeo.domain.challenge.dto.response.MemberChallengeResponse(" +
            "c.id, c.type, c.status, c.account.accountNo, c.startDate, c.endDate, " +
            "c.title, c.description, c.image, c.totalDeposit, c.participants, " +
            "mc.isSuccess, mc.deposit, mc.baseReward, mc.additionalReward, mc.totalScore) " +
            "FROM Challenge c " +
            "JOIN MemberChallenge mc ON mc.challenge.id = c.id " +
            "WHERE mc.member.id = :memberId " +
            "AND c.id = :challengeId")
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
            "WHERE mc.member.id = :memberId " +
            "AND c.type = :challengeType ")
    Optional<MemberChallenge> findByMemberIdAndChallengeType(UUID memberId, ChallengeType challengeType);


    @Query("SELECT mc " +
            "FROM MemberChallenge mc " +
            "JOIN FETCH Challenge c " +
            "ON mc.challenge.id = c.id " +
            "WHERE c.type = :challengeType " +
            "AND c.status = :challengeStatus ")
    List<MemberChallenge> findAllByChallengeTypeAndChallengeStatus(ChallengeType challengeType, ChallengeStatus challengeStatus);

    @Query("SELECT new com.shinhan.dongibuyeo.domain.challenge.dto.response.TopRankerInfo(m.nickname, m.email, m.profileImage, mc.totalScore) " +
            "FROM MemberChallenge mc " +
            "JOIN mc.member m " +
            "WHERE mc.challenge.id = :challengeId " +
            "ORDER BY mc.totalScore DESC")
    List<TopRankerInfo> findTop5ByChallengeId(@Param("challengeId") UUID challengeId);

    @Query("SELECT mc.totalScore FROM MemberChallenge mc WHERE mc.challenge.id = :challengeId ORDER BY mc.totalScore DESC")
    List<Integer> findAllScoresByChallengeId(@Param("challengeId") UUID challengeId);

    @Query("SELECT SUM(mc.baseReward) " +
            "FROM MemberChallenge mc " +
            "WHERE mc.challenge.id = :challengeId AND mc.isSuccess = false")
    Long getSumOfFailedBaseRewards(@Param("challengeId") UUID challengeId);

    @Query("SELECT COUNT(mc.id) " +
            "FROM MemberChallenge mc " +
            "WHERE mc.challenge.id = :challengeId AND mc.isSuccess = true")
    Integer getTotalCountOfSuccessMember(@Param("challengeId") UUID challengeId);

    @Query("SELECT NEW com.shinhan.dongibuyeo.domain.challenge.dto.response.ChallengeRewardStatistics(" +
            "SUM(CASE WHEN mc.totalScore >= :cutoffScore THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN mc.totalScore >= :cutoffScore THEN mc.additionalReward ELSE 0 END), " +
            "SUM(CASE WHEN mc.totalScore < :cutoffScore THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN mc.totalScore < :cutoffScore THEN mc.additionalReward ELSE 0 END)) " +
            "FROM MemberChallenge mc " +
            "WHERE mc.challenge.id = :challengeId AND mc.isSuccess = true")
    ChallengeRewardStatistics getChallengeRewardStatistics(
            @Param("challengeId") UUID challengeId,
            @Param("cutoffScore") Integer cutoffScore
    );
}
