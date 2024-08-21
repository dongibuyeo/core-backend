package com.shinhan.dongibuyeo.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DuplicateEmailResponse {
    private Boolean isPresent;
}
