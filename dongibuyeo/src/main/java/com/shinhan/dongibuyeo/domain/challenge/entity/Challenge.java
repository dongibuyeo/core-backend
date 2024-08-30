package com.shinhan.dongibuyeo.domain.challenge.entity;

import com.github.f4b6a3.ulid.UlidCreator;
import com.shinhan.dongibuyeo.domain.account.entity.Account;
import com.shinhan.dongibuyeo.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
public class Challenge extends BaseEntity {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id = UlidCreator.getMonotonicUlid().toUuid();

    @Enumerated(EnumType.STRING)
    private ChallengeType type;

    @OneToOne
    @JoinColumn(name = "account_id", unique = true)
    private Account account;

    private LocalDate startDate;
    private LocalDate endDate;

    private ChallengeStatus status = ChallengeStatus.SCHEDULED;

    private String title;
    private String description;
    private String image;

    private Long totalDeposit = 0L;

    private Integer participants = 0;

    @OneToMany(mappedBy = "challenge")
    private List<MemberChallenge> challengeMembers = new ArrayList<>();

    @Builder
    public Challenge(ChallengeType type, Account account, LocalDate startDate, LocalDate endDate, String title, String description, String image) {
        this.type = type;
        this.account = account;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = ChallengeStatus.SCHEDULED;
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public void updateAccount(Account account) {
        this.account = account;
    }

    public void addMember(MemberChallenge memberChallenge) {
        challengeMembers.add(memberChallenge);
        totalDeposit += memberChallenge.getDeposit();;
        participants++;
    }

    public void removeMember(MemberChallenge memberChallenge) {
        challengeMembers.remove(memberChallenge);
        totalDeposit -= memberChallenge.getDeposit();;
        participants--;
    }

    public void addTotalDeposit(Long deposit) {
        this.totalDeposit += totalDeposit;
    }

    public void updateStatus(ChallengeStatus status) {
        this.status = status;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateImage(String image) {
        this.image = image;
    }

    public void updateDate(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void updateDate(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        this.startDate = LocalDate.parse(startDate, formatter);
        this.endDate = LocalDate.parse(endDate, formatter);
    }

    public void updateChallengeType(ChallengeType type) {
        this.type = type;
    }
}
