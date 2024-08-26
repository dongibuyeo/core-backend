package com.shinhan.dongibuyeo.domain.chat.entity;

import com.shinhan.dongibuyeo.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
public class Room extends BaseEntity {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    private String name;

    @OneToMany(
            mappedBy = "room",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    private List<Message> messages = new ArrayList<>();

    public Room(String name) {
        this.name = name;
    }
}
