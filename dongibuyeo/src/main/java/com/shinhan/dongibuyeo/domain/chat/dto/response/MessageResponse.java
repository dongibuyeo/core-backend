package com.shinhan.dongibuyeo.domain.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private UUID memberId;
    private String memberNickName;
    private String message;
    private String image;
    private LocalDateTime createdAt;
}
