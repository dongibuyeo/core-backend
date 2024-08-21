package com.shinhan.dongibuyeo.domain.account.entity;

import com.github.f4b6a3.ulid.UlidCreator;
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
public class Account extends BaseEntity {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id = UlidCreator.getMonotonicUlid().toUuid();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String bankCode;

    private String accountNo;

    private String currency;

    private String currencyName;

    public Account(Member member, String bankCode, String accountNo, String currency, String currencyName) {
        this.member = member;
        member.getAccounts().add(this);
        this.bankCode = bankCode;
        this.accountNo = accountNo;
        this.currency = currency;
        this.currencyName = currencyName;
    }

}
