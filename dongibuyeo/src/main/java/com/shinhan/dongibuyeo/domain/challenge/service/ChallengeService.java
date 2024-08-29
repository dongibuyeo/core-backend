package com.shinhan.dongibuyeo.domain.challenge.service;

import com.shinhan.dongibuyeo.domain.account.dto.request.DepositRequest;
import com.shinhan.dongibuyeo.domain.account.dto.request.MakeAccountRequest;
import com.shinhan.dongibuyeo.domain.account.dto.response.MakeAccountResponse;
import com.shinhan.dongibuyeo.domain.account.entity.Account;
import com.shinhan.dongibuyeo.domain.account.exception.AccountNotFoundException;
import com.shinhan.dongibuyeo.domain.account.repository.AccountRepository;
import com.shinhan.dongibuyeo.domain.account.service.AccountService;
import com.shinhan.dongibuyeo.domain.challenge.dto.request.ChallengeRequest;
import com.shinhan.dongibuyeo.domain.challenge.dto.response.*;
import com.shinhan.dongibuyeo.domain.challenge.entity.Challenge;
import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeStatus;
import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeType;
import com.shinhan.dongibuyeo.domain.challenge.entity.MemberChallenge;
import com.shinhan.dongibuyeo.domain.challenge.exception.ChallengeNotCompletedException;
import com.shinhan.dongibuyeo.domain.challenge.exception.ChallengeNotFoundException;
import com.shinhan.dongibuyeo.domain.challenge.exception.MemberChallengeNotFoundException;
import com.shinhan.dongibuyeo.domain.challenge.mapper.ChallengeMapper;
import com.shinhan.dongibuyeo.domain.challenge.repository.ChallengeRepository;
import com.shinhan.dongibuyeo.domain.challenge.repository.MemberChallengeRepository;
import com.shinhan.dongibuyeo.domain.challenge.score.util.ScoreUtils;
import com.shinhan.dongibuyeo.domain.consume.service.ConsumeService;
import com.shinhan.dongibuyeo.domain.member.dto.response.MemberResponse;
import com.shinhan.dongibuyeo.domain.member.service.MemberService;
import com.shinhan.dongibuyeo.domain.product.entity.Product;
import com.shinhan.dongibuyeo.domain.product.service.ProductService;
import com.shinhan.dongibuyeo.domain.savings.dto.request.SavingProductRequest;
import com.shinhan.dongibuyeo.domain.savings.service.SavingsService;
import com.shinhan.dongibuyeo.global.entity.TransferType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class ChallengeService {

    private final MemberService memberService;

    private final ChallengeRepository challengeRepository;

    private final ChallengeMapper challengeMapper;
    private final AccountService accountService;
    private final ConsumeService consumeService;
    private final ProductService productService;
    private final AccountRepository accountRepository;
    private final SavingsService savingsService;
    private final MemberChallengeRepository memberChallengeRepository;

    @Value("${shinhan.savings.seven.bank-code}")
    private String bankCode;

    @Value("${shinhan.savings.seven.subscription-period}")
    private String subscriptionPeriod;

    @Value("${shinhan.savings.seven.min-balance}")
    private Long minBalance;

    @Value("${shinhan.savings.seven.max-balance}")
    private Long maxBalance;

    @Value("${shinhan.savings.seven.interest-rate}")
    private double savingInterestRate;

    @Value("${shinhan.quiz.deposit}")
    private Long deposit;

    @Value("${shinhan.quiz.head-count}")
    private Long headCount;

    @Value("${shinhan.challenge.estimate.success-rate}")
    private double successRate;

    @Value("${shinhan.challenge.estimate.bounce-back-rate}")
    private double bounceBackRate;

    @Value("${shinhan.challenge.interest-rate}")
    private double challengeInterestRate;

    public ChallengeService(MemberService memberService, ChallengeRepository challengeRepository, ChallengeMapper challengeMapper, AccountService accountService, ConsumeService consumeService, ProductService productService, AccountRepository accountRepository, SavingsService savingsService, MemberChallengeRepository memberChallengeRepository) {
        this.memberService = memberService;
        this.challengeRepository = challengeRepository;
        this.challengeMapper = challengeMapper;
        this.accountService = accountService;
        this.consumeService = consumeService;
        this.productService = productService;
        this.accountRepository = accountRepository;
        this.savingsService = savingsService;
        this.memberChallengeRepository = memberChallengeRepository;
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
        String accountName = challenge.getType().toString() + challenge.getStartDate().format(DateTimeFormatter.ofPattern("yyMMdd"));
        System.out.println("!!!!!!!!!!! acountName: " + accountName + "!!!!!!!!!!!!!!!!!");
        savingsService.makeSavingProduct(
                SavingProductRequest.builder()
                        .bankCode(bankCode)
                        .accountName(accountName)
                        .accountDescription(challenge.getTitle() + " 전용 계좌")
                        .subscriptionPeriod(subscriptionPeriod)
                        .minSubscriptionBalance(minBalance)
                        .maxSubscriptionBalance(maxBalance)
                        .interestRate(savingInterestRate)
                        .rateDescription(savingInterestRate + "%")
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

    public List<Challenge> findAllChallengesByStatusAndDate(ChallengeType type, LocalDate curDay) {
        return challengeRepository.findAllChallengesByStatusAndDate(type, curDay);
    }

    public ChallengeRankResponse getChallengeRank(UUID challengeId) {
        List<Integer> allScores = memberChallengeRepository.findAllScoresByChallengeId(challengeId);
        int totalParticipants = allScores.size();
        int top10PercentCutoff = calculateTop10PercentCutoff(allScores, totalParticipants);
        List<TopRankerInfo> top5RankerInfos = memberChallengeRepository.findTop5ByChallengeId(challengeId);

        return ChallengeRankResponse.builder()
                .challengeId(challengeId)
                .top10PercentCutoff(top10PercentCutoff)
                .top5Members(top5RankerInfos)
                .build();
    }

    public MemberChallengeRankResponse getMemberChallengeRank(UUID challengeId, UUID memberId) {
        List<Integer> allScores = memberChallengeRepository.findAllScoresByChallengeId(challengeId);
        int totalParticipants = allScores.size();
        int top10PercentCutoff = calculateTop10PercentCutoff(allScores, totalParticipants);

        MemberChallenge myChallenge = memberChallengeRepository.findMemberChallengeByChallengeIdAndMemberId(challengeId, memberId)
                .orElseThrow(() -> new MemberChallengeNotFoundException(challengeId, memberId));
        int myTotalScore = myChallenge.getTotalScore();

        int currentRank = allScores.indexOf(myTotalScore) + 1;
        double percentileRank = (double) currentRank / totalParticipants * 100;

        return MemberChallengeRankResponse.builder()
                .memberId(memberId)
                .challengeId(challengeId)
                .percentileRank(percentileRank)
                .totalScore(myTotalScore)
                .top10PercentCutoff(top10PercentCutoff)
                .build();
    }

    public static int calculateTop10PercentCutoff(List<Integer> scores, int totalParticipants) {
        if (scores.isEmpty()) return 0;

        int top10PercentIndex = Math.max((int) Math.ceil(totalParticipants * 0.1) - 1, 0);
        return scores.get(top10PercentIndex);
    }

    public AdditionalRewardResponse calculateEstimatedReward(UUID challengeId) {
        Challenge challenge = findChallengeById(challengeId);
        Long totalDeposit = challenge.getTotalDeposit();

        // 총 상금 계산
        int top10PercentMembersNum;
        int lower90PercentMembersNum;
        if (challenge.getStatus() != ChallengeStatus.COMPLETED) {   // 정산 전인 경우 성공률 기반으로 계산
            int totalParticipants = challenge.getParticipants();
            top10PercentMembersNum = Math.max(1, (int) Math.ceil(totalParticipants * successRate * 0.1));
            lower90PercentMembersNum = totalParticipants - top10PercentMembersNum;
        } else {
            int successParticipants = memberChallengeRepository.getTotalCountOfSuccessMember(challengeId);
            top10PercentMembersNum = successParticipants / 10;
            lower90PercentMembersNum = successParticipants - top10PercentMembersNum;
        }

        long leftDeposit = (long) Math.floor(totalDeposit * (1 - successRate) * bounceBackRate);
        return ScoreUtils.calculateEstimatedAdditionalRewardPerUnit(
                challengeInterestRate,
                totalDeposit,
                leftDeposit,
                top10PercentMembersNum,
                lower90PercentMembersNum
        );
    }

    public ChallengeResultResponse getChallengeResult(UUID challengeId) {
        // 챌린지 기본 정보
        Challenge challenge = findChallengeById(challengeId);

        if (challenge.getStatus() != ChallengeStatus.COMPLETED) {
            throw new ChallengeNotCompletedException(challengeId);
        }

        // 환급 조회를 위한 정보
        long totalDeposit = challenge.getTotalDeposit();
        long remainDeposit = memberChallengeRepository.getSumOfFailedBaseRewards(challengeId)
                + memberChallengeRepository.getSumOfSuccessBaseRewards(challengeId);

        int totalCountOfSuccessMember = memberChallengeRepository.getTotalCountOfSuccessMember(challengeId);
        int top10PercentMemberNum = totalCountOfSuccessMember / 10;
        int lower90PercentMemberNum = totalCountOfSuccessMember - top10PercentMemberNum;

        AdditionalRewardResponse additionalReward = ScoreUtils.calculateEstimatedAdditionalRewardPerUnit(
                challengeInterestRate,
                totalDeposit,
                remainDeposit,
                top10PercentMemberNum,
                lower90PercentMemberNum
        );

        double successRate = 0;
        int totalParticipants = challenge.getParticipants();
        if (totalParticipants != 0) {
            successRate = (double) totalCountOfSuccessMember / totalParticipants * 100;
        }

        successRate = Math.round(successRate * 100.0) / 100.0;

        return ChallengeResultResponse.builder()
                .challengeId(challengeId)
                .type(challenge.getType())
                .status(challenge.getStatus())
                .startDate(challenge.getStartDate())
                .endDate(challenge.getEndDate())
                .title(challenge.getTitle())
                .description(challenge.getDescription())
                .totalDeposit(totalDeposit)
                .participants(challenge.getParticipants()) // 챌린지 기본 정보
                .totalReward(additionalReward.getTotalReward())
                .interestEarned(additionalReward.getInterestEarned())
                .remainDeposit(additionalReward.getRemainDeposit())
                .top10PercentRewardPerUnit(additionalReward.getTop10PercentRewardPerUnit())
                .lower90PercentRewardPerUnit(additionalReward.getLower90PercentRewardPerUnit())
                .top10PercentMemberNum(top10PercentMemberNum)
                .lower90PercentMemberNum(lower90PercentMemberNum) // 환급 정산 정보
                .successNum(totalCountOfSuccessMember)
                .successRate(successRate)
                .build();
    }

    public MemberChallengeResultResponse getMemberChallengeResult(UUID challengeId, UUID memberId) {
        // 챌린지 기본 정보
        Challenge challenge = findChallengeById(challengeId);

        if (challenge.getStatus() != ChallengeStatus.COMPLETED) {
            throw new ChallengeNotCompletedException(challengeId);
        }

        // 환급 조회를 위한 정보
        long totalDeposit = challenge.getTotalDeposit();
        long remainDeposit = memberChallengeRepository.getSumOfFailedBaseRewards(challengeId)
                + memberChallengeRepository.getSumOfSuccessBaseRewards(challengeId);

        int totalCountOfSuccessMember = memberChallengeRepository.getTotalCountOfSuccessMember(challengeId);
        int top10PercentMemberNum = totalCountOfSuccessMember / 10;
        int lower90PercentMemberNum = totalCountOfSuccessMember - top10PercentMemberNum;

        AdditionalRewardResponse additionalReward = ScoreUtils.calculateEstimatedAdditionalRewardPerUnit(
                challengeInterestRate,
                totalDeposit,
                remainDeposit,
                top10PercentMemberNum,
                lower90PercentMemberNum
        );

        MemberChallenge memberChallenge = memberChallengeRepository.findMemberChallengeByChallengeIdAndMemberId(challengeId, memberId)
                .orElseThrow(() -> new MemberChallengeNotFoundException(challengeId, memberId));


        // 회원 소비 정보
        LocalDate challengeStartDate = challenge.getStartDate();
        LocalDate challengeEndDate = challenge.getEndDate();

        // 챌린지 기간과 동일한 길이의 직전 기간 계산
        long challengeDuration = ChronoUnit.DAYS.between(challengeStartDate, challengeEndDate);
        LocalDate previousPeriodStartDate = challengeStartDate.minusDays(challengeDuration);
        LocalDate previousPeriodEndDate = challengeStartDate.minusDays(1);

        TransferType transferType = challenge.getType().getTransferType();

        long currentPeriodConsumption = consumeService.getTotalConsumption(memberId, challengeStartDate, challengeEndDate, transferType);
        long previousPeriodConsumption = consumeService.getTotalConsumption(memberId, previousPeriodStartDate, previousPeriodEndDate, transferType);

        return MemberChallengeResultResponse.builder()
                .memberId(memberId)
                .challengeId(challengeId)
                .type(challenge.getType())
                .status(challenge.getStatus())
                .startDate(challenge.getStartDate())
                .endDate(challenge.getEndDate())
                .title(challenge.getTitle())
                .description(challenge.getDescription())        // 챌린지 정보
                .myStatus(memberChallenge.getStatus())
                .isSuccess(memberChallenge.getIsSuccess())
                .baseReward(memberChallenge.getBaseReward())
                .additionalReward(memberChallenge.getAdditionalReward())    // 멤버챌린지 정보
                .currentConsume(currentPeriodConsumption)
                .beforeConsume(previousPeriodConsumption)       // 소비 정보
                .top10PercentRewardPerUnit(additionalReward.getTop10PercentRewardPerUnit())
                .lower90PercentRewardPerUnit(additionalReward.getLower90PercentRewardPerUnit())
                .top10PercentMemberNum(additionalReward.getTop10PercentMemberNum())
                .lower90PercentMemberNum(additionalReward.getLower90PercentMemberNum()) // 정산 정보
                .build();
    }
}
