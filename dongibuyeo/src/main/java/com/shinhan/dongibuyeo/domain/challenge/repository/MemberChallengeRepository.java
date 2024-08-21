package com.shinhan.dongibuyeo.domain.challenge.repository;

import com.shinhan.dongibuyeo.domain.challenge.entity.MemberChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberChallengeRepository extends JpaRepository<MemberChallenge, UUID> {

    Optional<MemberChallenge> findMemberChallengeByChallengeIdAndMemberId(UUID challengeId, UUID memberId);
}
