package com.shinhan.dongibuyeo.domain.challenge.service;

import com.shinhan.dongibuyeo.domain.challenge.dto.request.ChallengeRequest;
import com.shinhan.dongibuyeo.domain.challenge.dto.request.JoinChallengeRequest;
import com.shinhan.dongibuyeo.domain.challenge.dto.response.ChallengeResponse;
import com.shinhan.dongibuyeo.domain.challenge.entity.Challenge;
import com.shinhan.dongibuyeo.domain.challenge.exception.ChallengeNotFoundException;
import com.shinhan.dongibuyeo.domain.challenge.mapper.ChallengeMapper;
import com.shinhan.dongibuyeo.domain.challenge.repository.ChallengeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeMapper challengeMapper;

    public ChallengeService(ChallengeRepository challengeRepository, ChallengeMapper challengeMapper) {
        this.challengeRepository = challengeRepository;
        this.challengeMapper = challengeMapper;
    }

    public List<ChallengeResponse> findAllChallenges() {
        return challengeRepository.findAll()
                .stream()
                .map(challengeMapper::toChallengeResponse)
                .toList();
    }

    public ChallengeResponse findChallengeByChallengeId(UUID challengeId) {
        return challengeRepository.findChallengeById(challengeId)
                .map(challengeMapper::toChallengeResponse)
                .orElseThrow(() -> new ChallengeNotFoundException(challengeId));
    }

    @Transactional
    public ChallengeResponse makeChallenge(ChallengeRequest request) {
        // TODO: request의 계좌 ID 기반으로 계좌를 조회해 계좌 연결하는 로직까지 추가
        Challenge challenge = challengeMapper.toChallenge(request);
        challengeRepository.save(challenge);
        return challengeMapper.toChallengeResponse(challenge);
    }

    @Transactional
    public void joinChallenge(JoinChallengeRequest request) {
        // MemberChallenge 생성

        //
    }


}
