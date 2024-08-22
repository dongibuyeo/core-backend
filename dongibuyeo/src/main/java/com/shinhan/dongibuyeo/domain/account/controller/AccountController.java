package com.shinhan.dongibuyeo.domain.account.controller;

import com.shinhan.dongibuyeo.domain.account.dto.request.DepositRequest;
import com.shinhan.dongibuyeo.domain.account.dto.request.MakeAccountRequest;
import com.shinhan.dongibuyeo.domain.account.dto.request.TransferRequest;
import com.shinhan.dongibuyeo.domain.account.dto.response.AccountDetailInfo;
import com.shinhan.dongibuyeo.domain.account.dto.response.DepositResponse;
import com.shinhan.dongibuyeo.domain.account.dto.response.MakeAccountResponse;
import com.shinhan.dongibuyeo.domain.account.dto.response.TransferResponse;
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
    public ResponseEntity<List<AccountDetailInfo>> getAllAccountsByMemberId(@PathVariable("memberId") UUID memberId) {
        return ResponseEntity.ok(accountService.getAllAccountsByMemberId(memberId));
    }

    @GetMapping("/member/{memberId}/{accountNo}")
    public ResponseEntity<AccountDetailInfo> getAccountByAccountId(@PathVariable("memberId") UUID memberId, @PathVariable("accountNo") String accountNo) {
        return ResponseEntity.ok(accountService.getAccountByAccountNo(memberId,accountNo));
    }

    @PostMapping("/transfer")
    public ResponseEntity<List<TransferResponse>> accountTransfer(@RequestBody TransferRequest request) {
       return ResponseEntity.ok(accountService.accountTransfer(request));
    }

    @PostMapping("/deposit")
    public ResponseEntity<DepositResponse> accountDeposit(@RequestBody DepositRequest request) {
        return ResponseEntity.ok(accountService.accountDeposit(request));
    }



    // 계좌 해지

    // 거래 내역
}
