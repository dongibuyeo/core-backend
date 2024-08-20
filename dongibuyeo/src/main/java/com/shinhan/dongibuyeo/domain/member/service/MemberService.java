package com.shinhan.dongibuyeo.domain.member.service;

import com.shinhan.dongibuyeo.domain.member.entity.Member;
import com.shinhan.dongibuyeo.domain.member.exception.MemberNotFoundException;
import com.shinhan.dongibuyeo.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member getMemberById(UUID id) {
        return memberRepository.findById(id).orElseThrow(() -> new MemberNotFoundException(id));
    }
}
