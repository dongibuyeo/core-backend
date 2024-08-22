package com.shinhan.dongibuyeo.domain.account.client;

import com.shinhan.dongibuyeo.domain.account.dto.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class AccountClient {
    private final WebClient webClient;

    public AccountClient(WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }

    public ShinhanMakeAccountResponse makeAccount(ShinhanMakeAccountRequest request) {
        return webClient.post()
                .uri("/edu/demandDeposit/createDemandDepositAccount")
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ShinhanMakeAccountResponse.class)
                .blockOptional()
                .orElseThrow();
    }

    public ShinhanGetAccountsResponse getAllAccountsByMember(ShinhanGetAccountsRequest request) {
        return webClient.post()
                .uri("/edu/demandDeposit/inquireDemandDepositAccountList")
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ShinhanGetAccountsResponse.class)
                .blockOptional()
                .orElseThrow();
    }

    public ShinhanGetAccountResponse getAccountByAccountNo(ShinhanGetAccountRequest request) {
        return webClient.post()
                .uri("/edu/demandDeposit/inquireDemandDepositAccount")
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ShinhanGetAccountResponse.class)
                .blockOptional()
                .orElseThrow();
    }

    public ShinhanTransferResponse accountTransfer(ShinhanTransferRequest request) {
        return webClient.post()
                .uri("/edu/demandDeposit/updateDemandDepositAccountTransfer")
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ShinhanTransferResponse.class)
                .doOnError(e -> log.info("[accountTransfer] header: {}", request))
                .blockOptional()
                .orElseThrow();
    }

    public ShinhanDepositResponse accountDeposit(ShinhanDepositRequest request) {
        return webClient.post()
                .uri("/edu/demandDeposit/updateDemandDepositAccountDeposit")
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ShinhanDepositResponse.class)
                .blockOptional()
                .orElseThrow();
    }
}
