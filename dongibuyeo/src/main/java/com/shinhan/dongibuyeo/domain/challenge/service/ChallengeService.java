package com.shinhan.dongibuyeo.domain.challenge.service;

import com.shinhan.dongibuyeo.domain.challenge.dto.request.ChallengeRequest;
import com.shinhan.dongibuyeo.domain.challenge.dto.request.JoinChallengeRequest;
import com.shinhan.dongibuyeo.domain.challenge.dto.response.ChallengeResponse;
import com.shinhan.dongibuyeo.domain.challenge.entity.Challenge;
import com.shinhan.dongibuyeo.domain.challenge.entity.MemberChallenge;
import com.shinhan.dongibuyeo.domain.challenge.exception.ChallengeNotFoundException;
import com.shinhan.dongibuyeo.domain.challenge.mapper.ChallengeMapper;
import com.shinhan.dongibuyeo.domain.challenge.repository.ChallengeRepository;
import com.shinhan.dongibuyeo.domain.challenge.repository.MemberChallengeRepository;
import com.shinhan.dongibuyeo.domain.member.entity.Member;
import com.shinhan.dongibuyeo.domain.member.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ChallengeService {

    private final MemberService memberService;

    private final ChallengeRepository challengeRepository;
    private final MemberChallengeRepository memberChallengeRepository;

    private final ChallengeMapper challengeMapper;

    public ChallengeService(MemberService memberService, ChallengeRepository challengeRepository, MemberChallengeRepository memberChallengeRepository, ChallengeMapper challengeMapper) {
        this.memberService = memberService;
        this.challengeRepository = challengeRepository;
        this.memberChallengeRepository = memberChallengeRepository;
        this.challengeMapper = challengeMapper;
    }

    private Challenge findChallengeById(UUID challengeId) {
        return challengeRepository.findChallengeById(challengeId)
                .orElseThrow(() -> new ChallengeNotFoundException(challengeId));
    }

    public List<ChallengeResponse> findAllChallenges() {
        return challengeRepository.findAll()
                .stream()
                .map(challengeMapper::toChallengeResponse)
                .toList();
    }

    public ChallengeResponse findChallengeByChallengeId(UUID challengeId) {
        return challengeMapper.toChallengeResponse(findChallengeById(challengeId));
    }

    @Transactional
    public ChallengeResponse makeChallenge(ChallengeRequest request) {
        // TODO: request의 계좌 ID 기반으로 계좌를 조회해 계좌 연결하는 로직까지 추가
        Challenge challenge = challengeMapper.toChallenge(request);
        challengeRepository.save(challenge);
        return challengeMapper.toChallengeResponse(challenge);
    }

    @Transactional
    public void deleteChallengeByChallengeId(UUID challengeId) {
        Challenge challenge = findChallengeById(challengeId);
        challenge.softDelete();
    }

    @Transactional
    public void joinChallenge(JoinChallengeRequest request) {
        // MemberChallenge 생성
        Member member = memberService.getMemberById(request.getMemberId());
        Challenge challenge = findChallengeById(request.getChallengeId());

        MemberChallenge memberChallenge = MemberChallenge.builder()
                .member(member)
                .challenge(challenge)
                .deposit(request.getDeposit())
                .build();

        memberChallengeRepository.save(memberChallenge);
        // 챌린지 정보 변경
        challenge.addMember(memberChallenge, request.getDeposit());
    }

}
