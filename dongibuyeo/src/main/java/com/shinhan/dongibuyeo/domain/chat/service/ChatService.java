package com.shinhan.dongibuyeo.domain.chat.service;

import com.shinhan.dongibuyeo.domain.chat.dto.request.MessageRequest;
import com.shinhan.dongibuyeo.domain.chat.dto.response.MessageResponse;
import com.shinhan.dongibuyeo.domain.chat.dto.response.RoomDetailResponse;
import com.shinhan.dongibuyeo.domain.chat.entity.Message;
import com.shinhan.dongibuyeo.domain.chat.entity.Room;
import com.shinhan.dongibuyeo.domain.chat.mapper.ChatRoomMapper;
import com.shinhan.dongibuyeo.domain.chat.repository.ChatRoomRepository;
import com.shinhan.dongibuyeo.domain.member.entity.Member;
import com.shinhan.dongibuyeo.domain.member.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    private final MemberService memberService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMapper mapper;

    public ChatService(MemberService memberService, ChatRoomRepository chatRoomRepository, ChatRoomMapper mapper) {
        this.memberService = memberService;
        this.chatRoomRepository = chatRoomRepository;
        this.mapper = mapper;
    }

    public Room getRoomByRoomName(String roomName) {
        return chatRoomRepository.findByNameAndChats(roomName).orElseThrow(EntityNotFoundException::new);
    }

    public RoomDetailResponse getRoomDetail(String roomName) {
        return mapper.getRoomDetail(getRoomByRoomName(roomName));
    }

    @Transactional
    public MessageResponse sendMessage(MessageRequest messageRequest) {
        Member member = memberService.getMemberById(messageRequest.getMemberId());
        Room room = chatRoomRepository.findByName(messageRequest.getRoomName()).orElseThrow(EntityNotFoundException::new);
        Message message = new Message(messageRequest.getMessage(),member,room,messageRequest.getSendAt());
        room.getMessages().add(message);
        return mapper.toMessageResponse(message);
    }

    @Transactional
    public void makeRoom() {
        chatRoomRepository.save(new Room("COFFEE"));
        chatRoomRepository.save(new Room("DELIVERY"));
        chatRoomRepository.save(new Room("DRINK"));
        chatRoomRepository.save(new Room("SEVEN"));
        chatRoomRepository.save(new Room("QUIZ"));
    }
}
