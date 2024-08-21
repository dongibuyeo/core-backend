package com.shinhan.dongibuyeo.domain.member.client;

import com.shinhan.dongibuyeo.domain.member.dto.client.ShinhanMemberRequest;
import com.shinhan.dongibuyeo.domain.member.dto.client.ShinhanMemberResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;

@Component
public class MemberClient {

    private final WebClient webClient;

    public MemberClient(WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }

    public ShinhanMemberResponse saveMember(ShinhanMemberRequest request) {
        return webClient.post()
                .uri("/member/")
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ShinhanMemberResponse.class)
                .blockOptional()
                .orElseThrow();
    }

    public ShinhanMemberResponse searchMember(ShinhanMemberRequest request) {
        return webClient.post()
                .uri("/member/search")
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ShinhanMemberResponse.class)
                .blockOptional()
                .orElseThrow();
    }
}
