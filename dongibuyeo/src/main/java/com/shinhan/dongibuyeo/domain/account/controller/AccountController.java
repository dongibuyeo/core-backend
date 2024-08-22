package com.shinhan.dongibuyeo.domain.account.controller;

import com.shinhan.dongibuyeo.domain.account.dto.request.MakeAccountRequest;
import com.shinhan.dongibuyeo.domain.account.dto.response.AccountDetailInfo;
import com.shinhan.dongibuyeo.domain.account.dto.response.MakeAccountResponse;
import com.shinhan.dongibuyeo.domain.account.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/personal")
    public ResponseEntity<MakeAccountResponse> makePersonalAccount(@RequestBody MakeAccountRequest request) {
        return ResponseEntity.ok(accountService.makePersonalAccount(request));
    }

    @PostMapping("/challenge")
    public ResponseEntity<MakeAccountResponse> makeChallengeAccount(@RequestBody MakeAccountRequest request) {
        return ResponseEntity.ok(accountService.makeChallengeAccount(request));
    }

    @GetMapping("/all/{memberId}")
    public ResponseEntity<List<AccountDetailInfo>> getAllAccountsByMemberId(@PathVariable UUID memberId) {
        return ResponseEntity.ok(accountService.getAllAccountsByMemberId(memberId));
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<Void> getAccountByAccountId(@PathVariable UUID accountId) {
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> terminateAccountByMemberId() {
        return ResponseEntity.ok().build();
    }


}
