package com.shinhan.dongibuyeo.domain.account.controller;

import com.shinhan.dongibuyeo.domain.account.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/personal")
    public ResponseEntity<Void> makePersonalAccount() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/challenge")
    public ResponseEntity<Void> makeChallengeAccount() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all/{memberId}")
    public ResponseEntity<Void> getAllAccountsByMemberId(@PathVariable UUID memberId) {
        return ResponseEntity.ok().build();
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
