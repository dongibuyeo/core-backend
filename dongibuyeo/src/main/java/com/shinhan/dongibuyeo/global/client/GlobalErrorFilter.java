package com.shinhan.dongibuyeo.global.client;

import com.shinhan.dongibuyeo.global.client.dto.ShinhanErrorResponse;
import com.shinhan.dongibuyeo.global.exception.ClientException;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

public class GlobalErrorFilter implements ExchangeFilterFunction {
    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        return next.exchange(request)
                .flatMap(this::handleStatus);
    }

    private Mono<ClientResponse> handleStatus(ClientResponse response) {
        if (response.statusCode().is4xxClientError() || response.statusCode().is5xxServerError()) {
            return response.bodyToMono(ShinhanErrorResponse.class)
                    .flatMap(shinhanErrorResponse -> Mono.error(
                            new ClientException(
                                    shinhanErrorResponse.getResponseCode(),
                                    shinhanErrorResponse.getResponseMessage()
                            )
                    ));
        } else {
            return Mono.just(response);
        }
    }
}