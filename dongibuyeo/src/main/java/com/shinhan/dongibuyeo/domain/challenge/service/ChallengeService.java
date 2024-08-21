package com.shinhan.dongibuyeo.domain.challenge.service;

import com.shinhan.dongibuyeo.domain.challenge.dto.response.ChallengeResponse;
import com.shinhan.dongibuyeo.domain.challenge.exception.ChallengeNotFoundException;
import com.shinhan.dongibuyeo.domain.challenge.mapper.ChallengeMapper;
import com.shinhan.dongibuyeo.domain.challenge.repository.ChallengeRepository;
import org.springframework.stereotype.Service;

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
}
