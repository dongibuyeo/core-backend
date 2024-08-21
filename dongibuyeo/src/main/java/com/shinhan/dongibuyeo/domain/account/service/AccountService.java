package com.shinhan.dongibuyeo.domain.account.service;

import com.shinhan.dongibuyeo.domain.account.client.AccountClient;
import com.shinhan.dongibuyeo.domain.account.dto.client.ShinhanMakeAccountResponse;
import com.shinhan.dongibuyeo.domain.account.dto.request.MakeAccountRequest;
import com.shinhan.dongibuyeo.domain.account.dto.response.AccountResponse;
import com.shinhan.dongibuyeo.domain.account.entity.Account;
import com.shinhan.dongibuyeo.domain.account.mapper.AccountMapper;
import com.shinhan.dongibuyeo.domain.account.repository.AccountRepository;
import com.shinhan.dongibuyeo.domain.member.entity.Member;
import com.shinhan.dongibuyeo.domain.member.service.MemberService;
import com.shinhan.dongibuyeo.global.header.GlobalUserHeader;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final AccountClient accountClient;

    private final AccountMapper accountMapper;

    private final MemberService memberService;

    public AccountService(AccountRepository accountRepository, AccountClient accountClient, AccountMapper accountMapper, MemberService memberService) {
        this.accountRepository = accountRepository;
        this.accountClient = accountClient;
        this.accountMapper = accountMapper;
        this.memberService = memberService;
    }

    @Transactional
    public AccountResponse makePersonalAccount(MakeAccountRequest request) {
        Member member = memberService.getMemberById(request.getMemberId());
        ShinhanMakeAccountResponse shinhanAccount = accountClient.makeAccount(accountMapper.toShinhanMakeAccountRequest(request,member));
        Account account = accountRepository.save(accountMapper.toAccountEntity(shinhanAccount,member));
        return accountMapper.toAccountResponse(account);
    }

    public void makeChallengeAccount() {}

    public void getAllAccountsByMemberId(UUID memberId) {}

    public void getAccountByAccountId(UUID accountId) {}

    public void terminateAccountByMemberId() {}
}
