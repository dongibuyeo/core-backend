package com.shinhan.dongibuyeo.domain.product.client;

import com.shinhan.dongibuyeo.domain.product.dto.client.ShinhanProductRequest;
import com.shinhan.dongibuyeo.domain.product.dto.client.ShinhanProductResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;

@Component
public class ProductClient {
    private final WebClient webClient;

    public ProductClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public ShinhanProductResponse makeProduct(ShinhanProductRequest request) {
        return webClient.post()
                .uri("/edu/demandDeposit/createDemandDeposit")
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ShinhanProductResponse.class)
                .blockOptional()
                .orElseThrow();
    }
}
