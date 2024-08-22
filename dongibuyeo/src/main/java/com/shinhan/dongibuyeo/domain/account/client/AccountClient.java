package com.shinhan.dongibuyeo.domain.account.client;

import com.shinhan.dongibuyeo.domain.account.dto.client.ShinhanGetAccountsRequest;
import com.shinhan.dongibuyeo.domain.account.dto.client.ShinhanGetAccountsResponse;
import com.shinhan.dongibuyeo.domain.account.dto.client.ShinhanMakeAccountRequest;
import com.shinhan.dongibuyeo.domain.account.dto.client.ShinhanMakeAccountResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

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


}
