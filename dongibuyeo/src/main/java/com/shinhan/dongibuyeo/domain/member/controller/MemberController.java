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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/members")
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
            @RequestParam @Valid String email) {
        return ResponseEntity.ok(memberService.login(email));
    }

    @GetMapping("/duplicate/{email}")
    public ResponseEntity<DuplicateEmailResponse> duplicateEmail(@PathVariable String email) {
        return ResponseEntity.ok(memberService.duplicateEmail(email));
    }

    @PatchMapping("/device")
    public ResponseEntity<Void> updateDeviceToken(@RequestBody DeviceTokenRequest request) {
        memberService.updateDeviceToken(request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/change")
    public ResponseEntity<Void> changeProfile(@RequestBody ProfileRequest request) {
        memberService.updateMemberProfile(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getAllMembers() {
        return ResponseEntity.ok(memberService.findAllMembers());
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> getMemberById(@PathVariable UUID memberId) {
        return ResponseEntity.ok(memberService.findMemberById(memberId));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<MemberResponse> getMemberByEmail(@PathVariable String email) {
        return ResponseEntity.ok(memberService.findMemberByEmail(email));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable UUID memberId) {
        memberService.deleteMemberById(memberId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin")
    public ResponseEntity<MemberResponse> getAdminMember() {
        return ResponseEntity.ok(memberService.findAdminMember());
    }
}
