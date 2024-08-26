package com.shinhan.dongibuyeo.domain.chat.controller;


import com.shinhan.dongibuyeo.domain.chat.dto.response.RoomDetailResponse;
import com.shinhan.dongibuyeo.domain.chat.service.ChatService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/{roomName}")
    public ResponseEntity<RoomDetailResponse> getRoomDetail(String roomName) {
        return ResponseEntity.ok(chatService.getRoomDetail(roomName));
    }
}
