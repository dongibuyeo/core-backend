package com.shinhan.dongibuyeo.domain.chat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinhan.dongibuyeo.domain.chat.dto.request.MessageRequest;
import com.shinhan.dongibuyeo.domain.chat.service.ChatService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketHandler extends TextWebSocketHandler implements InitializingBean {

    private Map<String, ArrayList<WebSocketSession>> rooms = new ConcurrentHashMap<>();
    private Map<WebSocketSession, String> users = new ConcurrentHashMap<>();
    private ChatService chatService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String[] roomName = {"COFFEE","DRINK","DELIVERY","SEVEN","QUIZ"};

    public WebSocketHandler(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        if(users.containsKey(session)) {
            rooms.get(users.get(session)).remove(session);
            users.remove(session);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String curMessage = message.getPayload();
        MessageRequest messageRequest = objectMapper.readValue(curMessage, MessageRequest.class);

        if(!users.containsKey(session)) {
            users.put(session, messageRequest.getRoomName());
            rooms.get(messageRequest.getRoomName()).add(session);
        }

        for(WebSocketSession other : rooms.get(users.get(session))) {
            if(other.equals(session))
                continue;

            other.sendMessage(message);
        }

        chatService.sendMessage(messageRequest);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for(int i = 0 , size = roomName.length ; i < size ; i++) {
            rooms.put(roomName[i], new ArrayList<>());
        }
    }
}

