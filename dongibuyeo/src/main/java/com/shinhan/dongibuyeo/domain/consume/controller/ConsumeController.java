package com.shinhan.dongibuyeo.domain.consume.controller;

import com.shinhan.dongibuyeo.domain.account.dto.response.TransactionHistory;
import com.shinhan.dongibuyeo.domain.consume.dto.request.ConsumptionRequest;
import com.shinhan.dongibuyeo.domain.consume.dto.request.MakeConsumptionRequest;
import com.shinhan.dongibuyeo.domain.consume.dto.response.ConsumptionResponse;
import com.shinhan.dongibuyeo.domain.consume.service.ConsumeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/consume")
public class ConsumeController {

    private final ConsumeService consumeService;

    public ConsumeController(ConsumeService consumeService) {
        this.consumeService = consumeService;
    }

    @PostMapping("/total")
    public ResponseEntity<ConsumptionResponse> getTotalBalance(@RequestBody ConsumptionRequest request) {
        return ResponseEntity.ok(consumeService.getTotalConsumtion(request));
    }

    @PostMapping
    public ResponseEntity<List<TransactionHistory>> getMonthTypeHistory(@RequestBody ConsumptionRequest request) {
        return ResponseEntity.ok(consumeService.getTypeHistory(request));
    }

    @PostMapping("/make")
    public ResponseEntity<Void> makeConsumption(@RequestBody MakeConsumptionRequest request) {
        consumeService.addConsumption(request);
        return ResponseEntity.noContent().build();
    }
}
