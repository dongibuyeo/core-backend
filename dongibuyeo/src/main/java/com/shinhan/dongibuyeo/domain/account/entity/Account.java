package com.shinhan.dongibuyeo.domain.account.entity;

import com.shinhan.dongibuyeo.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
public class Account extends BaseEntity {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
}
