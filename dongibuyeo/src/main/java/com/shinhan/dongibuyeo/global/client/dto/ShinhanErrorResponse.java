package com.shinhan.dongibuyeo.global.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShinhanErrorResponse {
    private String responseCode;
    private String responseMessage;
}
