package com.shinhan.dongibuyeo.domain.savings.controller;

import com.shinhan.dongibuyeo.domain.savings.dto.request.MakeSavingAccountRequest;
import com.shinhan.dongibuyeo.domain.savings.dto.request.MakeSevenSavingAccountRequest;
import com.shinhan.dongibuyeo.domain.savings.dto.request.SavingAccountRequest;
import com.shinhan.dongibuyeo.domain.savings.dto.request.SavingProductRequest;
import com.shinhan.dongibuyeo.domain.savings.dto.response.*;
import com.shinhan.dongibuyeo.domain.savings.service.SavingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/savings")
public class SavingsController {

    private final SavingsService savingsService;

    public SavingsController(SavingsService savingsService) {
        this.savingsService = savingsService;
    }

    @PostMapping("/product")
    public ResponseEntity<SavingInfo> makeSavingProduct(@RequestBody SavingProductRequest request) {
        return ResponseEntity.ok(savingsService.makeSavingProduct(request));
    }

    @GetMapping("/product")
    public ResponseEntity<List<SavingInfo>> getSavingProducts() {
        return ResponseEntity.ok(savingsService.getSavingProducts());
    }

    @PostMapping("/account")
    public ResponseEntity<SavingAccountInfo> makeSavingAccount(@RequestBody MakeSavingAccountRequest request) {
        return ResponseEntity.ok(savingsService.makeSavingAccount(request));
    }

    @DeleteMapping("/account")
    public ResponseEntity<DeleteSavingInfo> deleteSavingAccount(@RequestBody SavingAccountRequest request) {
        return ResponseEntity.ok(savingsService.deleteSavingAccounts(request.getMemberId(), request.getAccountNo()));
    }

    @PostMapping("/payment")
    public ResponseEntity<List<SavingPaymentInfo>> getSavingPayment(@RequestBody SavingAccountRequest request) {
        return ResponseEntity.ok(savingsService.getSavingPayment(request.getMemberId(), request.getAccountNo()));
    }

    @GetMapping("/all/{memberId}")
    public ResponseEntity<List<SavingAccountsDetail>> getAllSavingsByMemberId(@PathVariable("memberId") UUID memberId) {
        return ResponseEntity.ok(savingsService.getAllSavingsByMemberId(memberId));
    }

    @PostMapping("/account/seven")
    public ResponseEntity<SavingAccountInfo> makeSevenSavingAccount(@RequestBody MakeSevenSavingAccountRequest request) {
        return ResponseEntity.ok(savingsService.makeSevenSavingAccount(request));
    }
}
