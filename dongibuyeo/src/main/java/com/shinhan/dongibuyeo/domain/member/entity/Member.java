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

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 8)
    private String name;

    @Column(nullable = false, unique = true, length = 10)
    private String nickname;

    private String profileImage;

    private String apiKey;

    private String deviceToken;

    @Builder
    public Member(String email, String name, String nickname, String profileImage, String deviceToken) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.deviceToken = deviceToken;
    }

    public void updateApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}