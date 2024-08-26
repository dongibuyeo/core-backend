package com.shinhan.dongibuyeo.domain.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomDetailResponse {
    private RoomResponse roomInfo;
    private List<MessageResponse> messages;
}
