package com.shinhan.dongibuyeo.domain.challenge.service;

import com.shinhan.dongibuyeo.domain.account.dto.request.MakeAccountRequest;
import com.shinhan.dongibuyeo.domain.account.dto.response.MakeAccountResponse;
import com.shinhan.dongibuyeo.domain.account.entity.Account;
import com.shinhan.dongibuyeo.domain.account.exception.AccountNotFoundException;
import com.shinhan.dongibuyeo.domain.account.repository.AccountRepository;
import com.shinhan.dongibuyeo.domain.account.service.AccountService;
import com.shinhan.dongibuyeo.domain.challenge.dto.request.ChallengeRequest;
import com.shinhan.dongibuyeo.domain.challenge.dto.response.ChallengeResponse;
import com.shinhan.dongibuyeo.domain.challenge.entity.Challenge;
import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeStatus;
import com.shinhan.dongibuyeo.domain.challenge.exception.ChallengeNotFoundException;
import com.shinhan.dongibuyeo.domain.challenge.mapper.ChallengeMapper;
import com.shinhan.dongibuyeo.domain.challenge.repository.ChallengeRepository;
import com.shinhan.dongibuyeo.domain.member.dto.response.MemberResponse;
import com.shinhan.dongibuyeo.domain.member.service.MemberService;
import com.shinhan.dongibuyeo.domain.product.entity.Product;
import com.shinhan.dongibuyeo.domain.product.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ChallengeService {

    private final MemberService memberService;

    private final ChallengeRepository challengeRepository;

    private final ChallengeMapper challengeMapper;
    private final AccountService accountService;
    private final ProductService productService;
    private final AccountRepository accountRepository;

    public ChallengeService(MemberService memberService, ChallengeRepository challengeRepository, ChallengeMapper challengeMapper, AccountService accountService, ProductService productService, AccountRepository accountRepository) {
        this.memberService = memberService;
        this.challengeRepository = challengeRepository;
        this.challengeMapper = challengeMapper;
        this.accountService = accountService;
        this.productService = productService;
        this.accountRepository = accountRepository;
    }

    public Challenge findChallengeById(UUID challengeId) {
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

    public List<ChallengeResponse> findAllChallengesByStatus(ChallengeStatus status) {
        return challengeRepository.findChallengesByStatus(status)
                .stream()
                .map(challengeMapper::toChallengeResponse)
                .toList();
    }

    @Transactional
    public ChallengeResponse makeChallenge(ChallengeRequest request) {
        Challenge challenge = challengeMapper.toChallengeEntity(request);

        // ADMIN 회원으로 챌린지 계좌 생성
        MemberResponse adminMember = memberService.findAdminMember();
        Product adminProduct = productService.getAdminProduct();
        MakeAccountResponse accountResponse = accountService.makeChallengeAccount(
                new MakeAccountRequest(adminMember.getMemberId(),
                adminProduct.getAccountTypeUniqueNo()));

        Account account = accountRepository.findById(accountResponse.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException(accountResponse.getAccountId()));

        challenge.updateAccount(account);
        challengeRepository.save(challenge);
        return challengeMapper.toChallengeResponse(challenge);
    }

    @Transactional
    public void deleteChallengeByChallengeId(UUID challengeId) {
        Challenge challenge = findChallengeById(challengeId);
        challenge.softDelete();
    }

    @Transactional
    public ChallengeResponse updateChallengeByChallengeId(UUID challengeId, ChallengeRequest request) {
        Challenge challenge = findChallengeById(challengeId);

        challenge.updateChallengeType(request.getType());
        challenge.updateTitle(request.getTitle());
        challenge.updateDescription(request.getDescription());
        challenge.updateDate(request.getStartDate(), request.getEndDate());
        challenge.updateImage(request.getImage());

        return challengeMapper.toChallengeResponse(challenge);
    }

}
