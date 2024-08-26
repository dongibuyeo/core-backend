package com.shinhan.dongibuyeo.domain.consume.controller;

import com.shinhan.dongibuyeo.domain.consume.dto.request.ConsumtionRequest;
import com.shinhan.dongibuyeo.domain.consume.dto.response.ConsumtionResponse;
import com.shinhan.dongibuyeo.domain.consume.service.ConsumeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consume")
public class ConsumeController {

    private final ConsumeService consumeService;

    public ConsumeController(ConsumeService consumeService) {
        this.consumeService = consumeService;
    }

    @PostMapping
    public ResponseEntity<ConsumtionResponse> getTotalBalance(@RequestBody ConsumtionRequest request) {
        return ResponseEntity.ok(consumeService.getTotalConsumtion(request));
    }

}
