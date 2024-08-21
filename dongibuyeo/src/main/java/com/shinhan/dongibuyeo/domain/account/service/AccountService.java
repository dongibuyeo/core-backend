package com.shinhan.dongibuyeo.domain.account.service;

import com.shinhan.dongibuyeo.domain.account.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void makePersonalAccount() {

    }

    public void makeChallengeAccount() {

    }

    public void getAllAccountsByMemberId(UUID memberId) {

    }

    public void getAccountByAccountId(UUID accountId) {

    }

    public void terminateAccountByMemberId() {

    }
}
