package com.shinhan.dongibuyeo.domain.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRegisterRequest {
    private UUID memberId;
    private String token;
}
