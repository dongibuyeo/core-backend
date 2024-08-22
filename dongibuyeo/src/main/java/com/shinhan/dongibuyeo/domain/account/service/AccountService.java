package com.shinhan.dongibuyeo.domain.account.service;

import com.shinhan.dongibuyeo.domain.account.client.AccountClient;
import com.shinhan.dongibuyeo.domain.account.dto.client.*;
import com.shinhan.dongibuyeo.domain.account.dto.request.DepositRequest;
import com.shinhan.dongibuyeo.domain.account.dto.request.MakeAccountRequest;
import com.shinhan.dongibuyeo.domain.account.dto.request.TransferRequest;
import com.shinhan.dongibuyeo.domain.account.dto.response.AccountDetailInfo;
import com.shinhan.dongibuyeo.domain.account.dto.response.DepositResponse;
import com.shinhan.dongibuyeo.domain.account.dto.response.MakeAccountResponse;
import com.shinhan.dongibuyeo.domain.account.dto.response.TransferResponse;
import com.shinhan.dongibuyeo.domain.account.entity.Account;
import com.shinhan.dongibuyeo.domain.account.mapper.AccountMapper;
import com.shinhan.dongibuyeo.domain.account.repository.AccountRepository;
import com.shinhan.dongibuyeo.domain.member.entity.Member;
import com.shinhan.dongibuyeo.domain.member.service.MemberService;
import com.shinhan.dongibuyeo.global.header.GlobalUserHeader;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    @Value("${shinhan.key}")
    private String apiKey;

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
    public MakeAccountResponse makePersonalAccount(MakeAccountRequest request) {
        Member member = memberService.getMemberById(request.getMemberId());
        ShinhanMakeAccountResponse shinhanAccount = accountClient.makeAccount(accountMapper.toShinhanMakeAccountRequest(request,apiKey,member));
        Account account = accountRepository.save(accountMapper.toPersonalAccountEntity(shinhanAccount));
        account.updateMember(member);
        return accountMapper.toAccountResponse(account);
    }

    @Transactional
    public MakeAccountResponse makeChallengeAccount(MakeAccountRequest request) {
        Member member = memberService.getMemberById(request.getMemberId());
        ShinhanMakeAccountResponse shinhanAccount = accountClient.makeAccount(accountMapper.toShinhanMakeAccountRequest(request,apiKey,member));
        Account account = accountRepository.save(accountMapper.toPersonalAccountEntity(shinhanAccount));
        account.updateMember(member);
        return accountMapper.toAccountResponse(account);
    }

    @Transactional
    public List<AccountDetailInfo> getAllAccountsByMemberId(UUID memberId) {
        Member member = memberService.getMemberById(memberId);
        ShinhanGetAccountsResponse shinhanAccount = accountClient.getAllAccountsByMember(accountMapper.toShinhanGetAccountsRequest(apiKey,member.getUserKey()));

        shinhanAccount.getRec().forEach(
                x ->
                    member.getAccounts().add(accountMapper.detailToPersonalAccountEntity(x))
        );
        return shinhanAccount.getRec();
    }

    @Transactional
    public AccountDetailInfo getAccountByAccountNo(UUID memberId, String accountNo) {
        Member member = memberService.getMemberById(memberId);
        ShinhanGetAccountResponse shinhanAccount = accountClient.getAccountByAccountNo(
                accountMapper.toShinhanGetAccountRequest(apiKey,member.getUserKey(),accountNo)
        );

        member.getAccounts().add(accountMapper.detailToPersonalAccountEntity(shinhanAccount.getRec()));
        return shinhanAccount.getRec();
    }

    @Transactional
    public List<TransferResponse> accountTransfer(TransferRequest transferRequest) {
        Member member = memberService.getMemberById(transferRequest.getMemberId());

        ShinhanTransferResponse transfer = accountClient.accountTransfer(
            accountMapper.toShinhanTransferRequest(transferRequest,apiKey,member)
        );

        return transfer.getRec();
    }

    @Transactional
    public DepositResponse accountDeposit(DepositRequest depositRequest) {
        Member member = memberService.getMemberById(depositRequest.getMemberId());
        ShinhanDepositResponse deposit = accountClient.accountDeposit(accountMapper.toShinhanDepositRequest(depositRequest,apiKey,member));

        return deposit.getRec();
    }
}
