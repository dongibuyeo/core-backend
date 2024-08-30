package com.shinhan.dongibuyeo.domain.chat.controller;

import com.shinhan.dongibuyeo.domain.chat.dto.request.MessageRequest;
import com.shinhan.dongibuyeo.domain.chat.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatMessageController {

    private SimpMessagingTemplate messagingTemplate;
    private ChatService chatService;

    public ChatMessageController(SimpMessagingTemplate messagingTemplate,ChatService chatService) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
    }

    @MessageMapping("/chat")
    public void sendChat(MessageRequest messageRequest) {
        messagingTemplate.convertAndSend("/sub/chat/"+messageRequest.getRoomName(), messageRequest);
        chatService.sendMessage(messageRequest);
    }
}
