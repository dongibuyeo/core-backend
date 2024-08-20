package com.shinhan.dongibuyeo.member.controller;

import com.shinhan.dongibuyeo.member.service.MemberSevice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberSevice memberSevice;

    public MemberController(MemberSevice memberSevice) {
        this.memberSevice = memberSevice;
    }

}
