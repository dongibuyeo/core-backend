package com.shinhan.dongibuyeo.domain.member.controller;

import com.shinhan.dongibuyeo.domain.member.dto.request.MemberSaveRequest;
import com.shinhan.dongibuyeo.domain.member.dto.response.LoginResponse;
import com.shinhan.dongibuyeo.domain.member.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/save")
    public ResponseEntity<LoginResponse> saveMember(
            @RequestBody @Valid MemberSaveRequest memberSaveRequest) {
        return ResponseEntity.ok(memberService.saveMember(memberSaveRequest));
    }
}
