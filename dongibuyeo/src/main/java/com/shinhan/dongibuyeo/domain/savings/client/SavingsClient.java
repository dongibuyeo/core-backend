package com.shinhan.dongibuyeo.domain.savings.client;

import com.shinhan.dongibuyeo.domain.account.dto.client.*;
import com.shinhan.dongibuyeo.domain.savings.dto.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class SavingsClient {
    private final WebClient webClient;

    public SavingsClient(WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }

    public ShinhanMakeSavingResponse makeSavingProduct(ShinhanMakeSavingRequest request) {
        return webClient.post()
                .uri("/edu/savings/createProduct")
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ShinhanMakeSavingResponse.class)
                .blockOptional()
                .orElseThrow();
    }

    public ShinhanGetSavingsResponse getAllSavings(ShinhanGetSavingsRequest request) {
        return webClient.post()
                .uri("/edu/savings/inquireSavingsProducts")
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ShinhanGetSavingsResponse.class)
                .blockOptional()
                .orElseThrow();
    }

    public ShinhanMakeSavingAccountResponse makeSavingAccount(ShinhanMakeSavingAccountRequest request) {
        return webClient.post()
                .uri("/edu/savings/createAccount")
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ShinhanMakeSavingAccountResponse.class)
                .blockOptional()
                .orElseThrow();
    }

    public ShinhanGetSavingPaymentResponse getSavingPayment(ShinhanSavingRequest request) {
        return webClient.post()
                .uri("/edu/savings/inquirePayment")
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ShinhanGetSavingPaymentResponse.class)
                .blockOptional()
                .orElseThrow();
    }

    public ShinhanDeleteSavingResponse deleteSavingAccount(ShinhanSavingRequest request) {
        return webClient.post()
                .uri("/edu/savings/deleteAccount")
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ShinhanDeleteSavingResponse.class)
                .blockOptional()
                .orElseThrow();
    }

}
