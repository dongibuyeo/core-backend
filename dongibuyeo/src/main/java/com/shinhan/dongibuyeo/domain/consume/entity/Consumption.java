package com.shinhan.dongibuyeo.domain.consume.entity;

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
public class Consumption extends BaseEntity {
    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id = UlidCreator.getMonotonicUlid().toUuid();

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String transactionUniqueNo;
    private String transactionDate;
    private String transactionTime;
    private String transactionType;
    private String transactionTypeName;
    private String transactionAccountNo;
    private Long transactionBalance;
    private Long transactionAfterBalance;
    private String transactionSummary;
    private String transactionMemo;

    public Consumption(Member member, String transactionUniqueNo, String transactionTime, String transactionDate, String transactionType, String transactionTypeName, String transactionAccountNo, Long transactionBalance, Long transactionAfterBalance, String transactionSummary, String transactionMemo) {
        this.member = member;
        this.transactionUniqueNo = transactionUniqueNo;
        this.transactionTime = transactionTime;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
        this.transactionTypeName = transactionTypeName;
        this.transactionAccountNo = transactionAccountNo;
        this.transactionBalance = transactionBalance;
        this.transactionAfterBalance = transactionAfterBalance;
        this.transactionSummary = transactionSummary;
        this.transactionMemo = transactionMemo;
    }
}
