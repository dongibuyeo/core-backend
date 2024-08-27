package com.shinhan.dongibuyeo.domain.challenge.service;

import com.shinhan.dongibuyeo.domain.account.dto.request.DepositRequest;
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
import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeType;
import com.shinhan.dongibuyeo.domain.challenge.exception.ChallengeNotFoundException;
import com.shinhan.dongibuyeo.domain.challenge.mapper.ChallengeMapper;
import com.shinhan.dongibuyeo.domain.challenge.repository.ChallengeRepository;
import com.shinhan.dongibuyeo.domain.member.dto.response.MemberResponse;
import com.shinhan.dongibuyeo.domain.member.service.MemberService;
import com.shinhan.dongibuyeo.domain.product.entity.Product;
import com.shinhan.dongibuyeo.domain.product.service.ProductService;
import com.shinhan.dongibuyeo.domain.savings.dto.request.SavingProductRequest;
import com.shinhan.dongibuyeo.domain.savings.service.SavingsService;
import org.springframework.beans.factory.annotation.Value;
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
    private final SavingsService savingsService;

    @Value("${shinhan.savings.seven.bank-code}")
    private String bankCode;

    @Value("${shinhan.savings.seven.subscription-period}")
    private String subscriptionPeriod;

    @Value("${shinhan.savings.seven.min-balance}")
    private Long minBalance;

    @Value("${shinhan.savings.seven.max-balance}")
    private Long maxBalance;

    @Value("${shinhan.savings.seven.interest-rate}")
    private double interestRate;

    @Value("${shinhan.quiz.deposit}")
    private Long deposit;

    @Value("${shinhan.quiz.head-count}")
    private Long headCount;


    public ChallengeService(MemberService memberService, ChallengeRepository challengeRepository, ChallengeMapper challengeMapper, AccountService accountService, ProductService productService, AccountRepository accountRepository, SavingsService savingsService) {
        this.memberService = memberService;
        this.challengeRepository = challengeRepository;
        this.challengeMapper = challengeMapper;
        this.accountService = accountService;
        this.productService = productService;
        this.accountRepository = accountRepository;
        this.savingsService = savingsService;
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
        return findAllByStatus(status)
                .stream()
                .map(challengeMapper::toChallengeResponse)
                .toList();
    }

    /**
     * 챌린지 생성 메서드
     * <p>
     * - 소비: 챌린지 계좌 생성
     * - 적금: 챌린지 계좌 생성, 적금 상품 생성(적금상품명은 챌린지 제목과 동일)
     * - 퀴즈: 챌린지 계좌 생성, 기관 입금
     *
     * @param request
     * @return
     */
    @Transactional
    public ChallengeResponse makeChallenge(ChallengeRequest request) {
        Challenge challenge = challengeMapper.toChallengeEntity(request);

        Account adminAccount = createChallengeAccount();
        challenge.updateAccount(adminAccount);

        processChallengeByType(request.getType(), challenge, adminAccount);
        challengeRepository.save(challenge);
        return challengeMapper.toChallengeResponse(challenge);
    }

    private Account createChallengeAccount() {
        MemberResponse adminMember = memberService.findAdminMember();
        Product adminProduct = productService.getAdminProduct();

        MakeAccountResponse accountResponse = accountService.makeChallengeAccount(
                new MakeAccountRequest(adminMember.getMemberId(),
                        adminProduct.getAccountTypeUniqueNo()));

        return accountRepository.findById(accountResponse.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException(accountResponse.getAccountId()));
    }

    private void processChallengeByType(ChallengeType type, Challenge challenge, Account adminAccount) {
        switch (type) {
            case SAVINGS_SEVEN:
                createSavingsProduct(challenge);
                break;
            case QUIZ_SOLBEING:
                depositToQuizAccount(adminAccount);
                break;
            default:
        }
    }

    private void createSavingsProduct(Challenge challenge) {
        savingsService.makeSavingProduct(
                SavingProductRequest.builder()
                        .bankCode(bankCode)
                        .accountName(challenge.getTitle())
                        .accountDescription(challenge.getTitle() + " 전용 계좌")
                        .subscriptionPeriod(subscriptionPeriod)
                        .minSubscriptionBalance(minBalance)
                        .maxSubscriptionBalance(maxBalance)
                        .interestRate(interestRate)
                        .rateDescription(interestRate + "%")
                        .build()
        );
    }

    private void depositToQuizAccount(Account adminAccount) {
        MemberResponse adminMember = memberService.findAdminMember();
        accountService.accountDeposit(
                new DepositRequest(
                        adminMember.getMemberId(),
                        adminAccount.getAccountNo(),
                        deposit * headCount
                )
        );
    }

    @Transactional
    public void deleteChallengeByChallengeId(UUID challengeId) {
        Challenge challenge = findChallengeById(challengeId);
        if (challenge.getStatus() == ChallengeStatus.SCHEDULED) {
            challenge.softDelete();
        }
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

    public List<Challenge> findAllByStatus(ChallengeStatus status) {
        return challengeRepository.findAllChallengesByStatus(status);
    }

}
