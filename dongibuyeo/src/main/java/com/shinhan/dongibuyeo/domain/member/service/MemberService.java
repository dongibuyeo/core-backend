package com.shinhan.dongibuyeo.domain.member.service;

import com.shinhan.dongibuyeo.domain.member.client.MemberClient;
import com.shinhan.dongibuyeo.domain.member.dto.client.ShinhanMemberRequest;
import com.shinhan.dongibuyeo.domain.member.dto.client.ShinhanMemberResponse;
import com.shinhan.dongibuyeo.domain.member.dto.request.DeviceTokenRequest;
import com.shinhan.dongibuyeo.domain.member.dto.request.MemberLoginRequest;
import com.shinhan.dongibuyeo.domain.member.dto.request.MemberSaveRequest;
import com.shinhan.dongibuyeo.domain.member.dto.request.ProfileRequest;
import com.shinhan.dongibuyeo.domain.member.dto.response.DuplicateEmailResponse;
import com.shinhan.dongibuyeo.domain.member.dto.response.MemberResponse;
import com.shinhan.dongibuyeo.domain.member.entity.Member;
import com.shinhan.dongibuyeo.domain.member.exception.MemberNotFoundException;
import com.shinhan.dongibuyeo.domain.member.mapper.MemberMapper;
import com.shinhan.dongibuyeo.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class MemberService {

    @Value("${shinhan.key}")
    private String apiKey;

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    private final MemberClient memberClient;

    public MemberService(MemberRepository memberRepository, MemberMapper memberMapper, MemberClient memberClient) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
        this.memberClient = memberClient;
    }

    public Member getMemberById(UUID id) {
        return memberRepository.findById(id).orElseThrow(() -> new MemberNotFoundException(id));
    }

    public Member getMemberByEmail(String email) {
        return memberRepository.findMemberByEmail(email).orElseThrow(() -> new MemberNotFoundException(email));
    }

    /**
     * 회원 등록 메서드
     * 1. 금융 API 내 유저 등록 및 api key 발급
     * 2. api key, 추가 정보(이메일, 이름, 닉네임, 프로필 이미지, FCM 토큰)로 회원 가입 처리
     */
    @Transactional
    public MemberResponse saveMember(MemberSaveRequest request) {
        ShinhanMemberResponse response = saveUserKey(request.getEmail());

        Member member = memberMapper.toMemberEntity(request);
        member.updateUserKey(response.getUserKey());
        memberRepository.save(member);

        return memberMapper.toMemberResponse(member);
    }

    private ShinhanMemberResponse saveUserKey(String email) {
        return memberClient.saveMember(new ShinhanMemberRequest(apiKey, email));
    }

    public DuplicateEmailResponse duplicateEmail(String email) {
        boolean isPresent = isDuplicateEmail(email);
        return new DuplicateEmailResponse(isPresent);
    }

    public boolean isDuplicateEmail(String email) {
        try {
            memberClient.searchMember(new ShinhanMemberRequest(apiKey, email));
        } catch (MemberNotFoundException e) {
            return false;
        }
        return true;
    }

    public MemberResponse login(MemberLoginRequest request) {
        Member member = getMemberByEmail(request.getEmail());
        return memberMapper.toMemberResponse(member);
    }

    @Transactional
    public void updateDeviceToken(DeviceTokenRequest request) {
        Member member = getMemberById(request.getMemberId());
        member.updateDeviceToken(request.getDeviceToken());
    }

    @Transactional
    public void updateMemberProfile(ProfileRequest request) {
        Member member = getMemberById(request.getMemberId());
        member.changeNickname(request.getNickname());
        member.changeProfileImage(request.getProfileImage());
    }

    public List<MemberResponse> findMembers() {
        return memberRepository.findAll()
                .stream()
                .map(memberMapper::toMemberResponse)
                .toList();
    }

    public MemberResponse findMemberById(UUID memberId) {
        Member member = getMemberById(memberId);
        return memberMapper.toMemberResponse(member);
    }

    public MemberResponse findMemberByEmail(String email) {
        Member member = getMemberByEmail(email);
        return memberMapper.toMemberResponse(member);
    }

    @Transactional
    public void deleteMemberById(UUID memberId) {
        Member member = getMemberById(memberId);
        member.softDelete();
    }
}
