package com.shinhan.dongibuyeo.domain.member.entity;

import com.github.f4b6a3.ulid.UlidCreator;
import com.shinhan.dongibuyeo.domain.account.entity.Account;
import com.shinhan.dongibuyeo.global.entity.BaseEntity;
import io.netty.util.internal.ConcurrentSet;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.*;

@Entity
@Getter
@EqualsAndHashCode(of = "nickname")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
public class Member extends BaseEntity {

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

    private String userKey;

    private String deviceToken;

    @OneToMany(
            mappedBy = "member",
            cascade = {CascadeType.PERSIST,CascadeType.MERGE}
    )
    private Set<Account> accounts = new HashSet<>();

    @Builder
    public Member(String email, String name, String nickname, String profileImage, String deviceToken) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.deviceToken = deviceToken;
    }

    public void updateUserKey(String userKey) {
        this.userKey = userKey;
    }

    public void updateDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
