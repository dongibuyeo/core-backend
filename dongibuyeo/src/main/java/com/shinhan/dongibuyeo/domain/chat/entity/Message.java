package com.shinhan.dongibuyeo.domain.chat.entity;

import com.shinhan.dongibuyeo.domain.member.entity.Member;
import com.shinhan.dongibuyeo.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
public class Message extends BaseEntity {
    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    private String message;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    public Message(String message, Member member, Room room) {
        this.message = message;
        this.member = member;
        this.room = room;
    }
}
