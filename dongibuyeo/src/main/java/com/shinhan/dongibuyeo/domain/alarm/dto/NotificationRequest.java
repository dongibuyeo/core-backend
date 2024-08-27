package com.shinhan.dongibuyeo.domain.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    private String token;
    private String title;
    private String content;
}
