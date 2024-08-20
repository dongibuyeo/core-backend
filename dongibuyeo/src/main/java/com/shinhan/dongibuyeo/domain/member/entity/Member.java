package com.shinhan.dongibuyeo.domain.member.entity;

import com.github.f4b6a3.ulid.UlidCreator;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@EqualsAndHashCode(of = "nickname")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id = UlidCreator.getMonotonicUlid().toUuid();

    @Column(nullable = false, length = 8)
    private String name;

    @Column(nullable = false, unique = true, length = 10)
    private String nickname;

    private String profileImage;

    private String apiKey;

    private String deviceToken;

    @Builder
    public Member(UUID id, String name, String nickname, String profileImage, String apiKey, String deviceToken) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.apiKey = apiKey;
        this.deviceToken = deviceToken;
    }
}