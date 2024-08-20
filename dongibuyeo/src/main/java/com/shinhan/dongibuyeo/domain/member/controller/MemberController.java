package com.shinhan.dongibuyeo.domain.member.controller;

import com.shinhan.dongibuyeo.domain.member.dto.request.MemberLoginRequest;
import com.shinhan.dongibuyeo.domain.member.dto.request.MemberSaveRequest;
import com.shinhan.dongibuyeo.domain.member.dto.response.DuplicateEmailResponse;
import com.shinhan.dongibuyeo.domain.member.dto.response.MemberLoginResponse;
import com.shinhan.dongibuyeo.domain.member.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/save")
    public ResponseEntity<MemberLoginResponse> saveMember(
            @RequestBody @Valid MemberSaveRequest memberSaveRequest) {
        return ResponseEntity.ok(memberService.saveMember(memberSaveRequest));
    }

    @GetMapping("/login")
    public ResponseEntity<MemberLoginResponse> login(
            @RequestBody @Valid MemberLoginRequest memberLoginRequest) {
        return ResponseEntity.ok(memberService.login(memberLoginRequest));
    }

    @GetMapping("/duplicate/{email}")
    public ResponseEntity<DuplicateEmailResponse> duplicateNickname(@PathVariable String email) {
        return ResponseEntity.ok(memberService.duplicateEmail(email));
    }

}
