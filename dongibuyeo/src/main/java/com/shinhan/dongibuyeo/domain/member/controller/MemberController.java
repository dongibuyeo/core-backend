package com.shinhan.dongibuyeo.domain.member.controller;

import com.shinhan.dongibuyeo.domain.member.dto.request.DeviceTokenRequest;
import com.shinhan.dongibuyeo.domain.member.dto.request.MemberLoginRequest;
import com.shinhan.dongibuyeo.domain.member.dto.request.MemberSaveRequest;
import com.shinhan.dongibuyeo.domain.member.dto.request.ProfileRequest;
import com.shinhan.dongibuyeo.domain.member.dto.response.DuplicateEmailResponse;
import com.shinhan.dongibuyeo.domain.member.dto.response.MemberResponse;
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
    public ResponseEntity<MemberResponse> saveMember(
            @RequestBody @Valid MemberSaveRequest memberSaveRequest) {
        return ResponseEntity.ok(memberService.saveMember(memberSaveRequest));
    }

    @GetMapping("/login")
    public ResponseEntity<MemberResponse> login(
            @RequestBody @Valid MemberLoginRequest memberLoginRequest) {
        return ResponseEntity.ok(memberService.login(memberLoginRequest));
    }

    @GetMapping("/duplicate/{email}")
    public ResponseEntity<DuplicateEmailResponse> duplicateNickname(@PathVariable String email) {
        return ResponseEntity.ok(memberService.duplicateEmail(email));
    }

    @PostMapping("/device")
    public ResponseEntity<Void> updateDeviceToken(@RequestBody DeviceTokenRequest request) {
        memberService.updateDeviceToken(request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/change")
    public ResponseEntity<Void> changeProfile(@RequestBody ProfileRequest request) {
        memberService.updateMemberProfile(request);
        return ResponseEntity.noContent().build();
    }
}
