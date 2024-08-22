package com.shinhan.dongibuyeo.domain.challenge.service;

import com.shinhan.dongibuyeo.domain.account.dto.request.MakeAccountRequest;
import com.shinhan.dongibuyeo.domain.account.service.AccountService;
import com.shinhan.dongibuyeo.domain.challenge.dto.request.ChallengeRequest;
import com.shinhan.dongibuyeo.domain.challenge.dto.request.JoinChallengeRequest;
import com.shinhan.dongibuyeo.domain.challenge.dto.response.ChallengeResponse;
import com.shinhan.dongibuyeo.domain.challenge.dto.response.MemberChallengeResponse;
import com.shinhan.dongibuyeo.domain.challenge.entity.Challenge;
import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeStatus;
import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeType;
import com.shinhan.dongibuyeo.domain.challenge.entity.MemberChallenge;
import com.shinhan.dongibuyeo.domain.challenge.exception.ChallengeAlreadyStartedException;
import com.shinhan.dongibuyeo.domain.challenge.exception.ChallengeCannotWithdrawException;
import com.shinhan.dongibuyeo.domain.challenge.exception.ChallengeNotFoundException;
import com.shinhan.dongibuyeo.domain.challenge.exception.MemberChallengeNotFoundException;
import com.shinhan.dongibuyeo.domain.challenge.mapper.ChallengeMapper;
import com.shinhan.dongibuyeo.domain.challenge.repository.ChallengeRepository;
import com.shinhan.dongibuyeo.domain.challenge.repository.MemberChallengeRepository;
import com.shinhan.dongibuyeo.domain.member.dto.response.MemberResponse;
import com.shinhan.dongibuyeo.domain.member.entity.Member;
import com.shinhan.dongibuyeo.domain.member.service.MemberService;
import com.shinhan.dongibuyeo.domain.product.dto.request.MakeProductRequest;
import com.shinhan.dongibuyeo.domain.product.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ChallengeService {

    private final MemberService memberService;

    private final ChallengeRepository challengeRepository;
    private final MemberChallengeRepository memberChallengeRepository;

    private final ChallengeMapper challengeMapper;
    private final AccountService accountService;
    private final ProductService productService;

    public ChallengeService(MemberService memberService, ChallengeRepository challengeRepository, MemberChallengeRepository memberChallengeRepository, ChallengeMapper challengeMapper, AccountService accountService, ProductService productService) {
        this.memberService = memberService;
        this.challengeRepository = challengeRepository;
        this.memberChallengeRepository = memberChallengeRepository;
        this.challengeMapper = challengeMapper;
        this.accountService = accountService;
        this.productService = productService;
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

    public List<ChallengeResponse> findAllChallengesByStatus(ChallengeStatus status) {
        return challengeRepository.findChallengesByStatus(status)
                .stream()
                .map(challengeMapper::toChallengeResponse)
                .toList();
    }

    public List<ChallengeResponse> findAllChallengesByMemberId(UUID memberId) {
        return memberChallengeRepository.findChallengesByMemberId(memberId)
                .orElseGet(ArrayList::new)
                .stream()
                .map(challengeMapper::toChallengeResponse)
                .toList();
    }

    @Transactional
    public ChallengeResponse makeChallenge(ChallengeRequest request) {
        // TODO request의 계좌 ID 기반으로 계좌를 조회해 계좌 연결하는 로직까지 추가
        Challenge challenge = challengeMapper.toChallengeEntity(request);
        MemberResponse adminMember = memberService.findAdminMember();
        productService.makeProduct(new MakeProductRequest());
//        accountService.makeChallengeAccount(new MakeAccountRequest(adminMember.getMemberId(), ));

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

        // 중도 참여 불가
        if (LocalDate.now().isAfter(challenge.getStartDate())) {
            throw new ChallengeAlreadyStartedException(challenge.getId());
        }

        MemberChallenge memberChallenge = MemberChallenge.builder()
                .member(member)
                .challenge(challenge)
                .deposit(request.getDeposit())
                .build();

        // 챌린지 정보 변경
        challenge.addMember(memberChallenge);
        member.addChallenge(memberChallenge);
    }

    @Transactional
    public void cancelJoinChallenge(UUID memberId, UUID challengeId) {
        Member member = memberService.getMemberById(memberId);
        Challenge challenge = findChallengeById(challengeId);

        // 중도 취소 불가
        if (LocalDate.now().isAfter(challenge.getStartDate())) {
            throw new ChallengeAlreadyStartedException(challengeId);
        }

        MemberChallenge memberChallenge = getMemberChallenge(challengeId, memberId);
        challenge.removeMember(memberChallenge);
        member.removeChallenge(memberChallenge);
        memberChallenge.softDelete();

        // TODO 보증금 반환 로직
    }

    @Transactional
    public void withdrawChallenge(UUID challengeId, UUID memberId) {
        Challenge challenge = findChallengeById(challengeId);
        Member member = memberService.getMemberById(memberId);
        MemberChallenge memberChallenge = getMemberChallenge(challengeId, memberId);

        // 적금의 경우만 중도 해지 가능
        if(challenge.getType() != ChallengeType.SAVINGS_SEVEN) {
            throw new ChallengeCannotWithdrawException(challenge.getType());
        }

        // TODO 예치금 조회 후 반환 로직 (챌린지 계좌 -> 유저 챌린지 계좌)
        Long userDeposit = memberChallenge.getDeposit();


        // 챌린지, 참여 이력 수정
        challenge.removeMember(memberChallenge);
        member.removeChallenge(memberChallenge);
        memberChallenge.softDelete();

        // TODO 적금 해지 후 환급 로직 (유저 적금 계좌 -> 유저 챌린지 계좌)

    }

    private MemberChallenge getMemberChallenge(UUID challengeId, UUID memberId) {
        return memberChallengeRepository.findMemberChallengeByChallengeIdAndMemberId(challengeId, memberId)
                .orElseThrow(() -> new MemberChallengeNotFoundException(challengeId, memberId));
    }

    public MemberChallengeResponse findChallengeByChallengeIdAndMemberId(UUID challengeId, UUID memberId) {
        return memberChallengeRepository.findChallengeByMemberIdAndChallengeId(challengeId, memberId)
                .orElseThrow(() -> new MemberChallengeNotFoundException(challengeId, memberId));
    }

    public List<ChallengeResponse> findAllChallengesByMemberIdAndStatus(UUID memberId, ChallengeStatus status) {
        return challengeRepository.findChallengesByMemberIdAndStatus(memberId, status);
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
