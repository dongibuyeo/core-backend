package com.shinhan.dongibuyeo.member.service;

import com.shinhan.dongibuyeo.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberSevice {

    private final MemberRepository memberRepository;

    public MemberSevice(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
}
