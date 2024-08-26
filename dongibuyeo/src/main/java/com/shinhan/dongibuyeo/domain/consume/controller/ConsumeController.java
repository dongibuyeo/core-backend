package com.shinhan.dongibuyeo.domain.consume.controller;

import com.shinhan.dongibuyeo.domain.account.dto.response.TransactionHistory;
import com.shinhan.dongibuyeo.domain.consume.dto.request.ConsumtionRequest;
import com.shinhan.dongibuyeo.domain.consume.dto.response.ConsumtionResponse;
import com.shinhan.dongibuyeo.domain.consume.service.ConsumeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/consume")
public class ConsumeController {

    private final ConsumeService consumeService;

    public ConsumeController(ConsumeService consumeService) {
        this.consumeService = consumeService;
    }

    @PostMapping("/total")
    public ResponseEntity<ConsumtionResponse> getTotalBalance(@RequestBody ConsumtionRequest request) {
        return ResponseEntity.ok(consumeService.getTotalConsumtion(request));
    }

    @PostMapping
    public ResponseEntity<List<TransactionHistory>> getMonthTypeHistory(@RequestBody ConsumtionRequest request) {
        return ResponseEntity.ok(consumeService.getTypeHistory(request));
    }
}
