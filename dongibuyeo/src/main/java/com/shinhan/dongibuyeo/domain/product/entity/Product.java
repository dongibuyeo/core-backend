package com.shinhan.dongibuyeo.domain.product.entity;

import com.github.f4b6a3.ulid.UlidCreator;
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
public class Product extends BaseEntity {
    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id = UlidCreator.getMonotonicUlid().toUuid();
    private String accountTypeUniqueNo;
    private String bankCode;
    private String bankName;
    private String accountTypeCode;
    private String accountTypeName;
    private String accountName;
    private String accountDescription;
    private String accountType;

    public Product(String accountTypeUniqueNo, String bankCode, String bankName, String accountTypeCode, String accountTypeName, String accountName, String accountDescription, String accountType) {
        this.accountTypeUniqueNo = accountTypeUniqueNo;
        this.bankCode = bankCode;
        this.bankName = bankName;
        this.accountTypeCode = accountTypeCode;
        this.accountTypeName = accountTypeName;
        this.accountName = accountName;
        this.accountDescription = accountDescription;
        this.accountType = accountType;
    }
}
