package com.shinhan.dongibuyeo.domain.chat.mapper;

import com.shinhan.dongibuyeo.domain.chat.dto.response.MessageResponse;
import com.shinhan.dongibuyeo.domain.chat.dto.response.RoomDetailResponse;
import com.shinhan.dongibuyeo.domain.chat.dto.response.RoomResponse;
import com.shinhan.dongibuyeo.domain.chat.entity.Message;
import com.shinhan.dongibuyeo.domain.chat.entity.Room;
import org.springframework.stereotype.Component;

@Component
public class ChatRoomMapper {
    public RoomDetailResponse getRoomDetail(Room room) {
        return new RoomDetailResponse(
                toSimpleResponse(room),
                room.getMessages().stream().map(this::toMessageResponse).toList()
        );
    }

    public RoomResponse toSimpleResponse(Room room) {
        return new RoomResponse(
                room.getId(),
                room.getName()
        );
    }

    public MessageResponse toMessageResponse(Message message) {
        return new MessageResponse(
                message.getMember().getId(),
                message.getMember().getName(),
                message.getMessage(),
                message.getMember().getProfileImage(),
                message.getCreatedAt()
        );
    }
}
