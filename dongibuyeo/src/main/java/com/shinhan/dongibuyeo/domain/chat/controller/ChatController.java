package com.shinhan.dongibuyeo.domain.chat.controller;


import com.shinhan.dongibuyeo.domain.chat.dto.response.RoomDetailResponse;
import com.shinhan.dongibuyeo.domain.chat.service.ChatService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/{roomName}")
    public ResponseEntity<RoomDetailResponse> getRoomDetail(@PathVariable("roomName") String roomName) {
        return ResponseEntity.ok(chatService.getRoomDetail(roomName));
    }

    @PostMapping("/makeRoom")
    public ResponseEntity<Void> makeRoom() {
        chatService.makeRoom();
        return ResponseEntity.ok().build();
    }
}
