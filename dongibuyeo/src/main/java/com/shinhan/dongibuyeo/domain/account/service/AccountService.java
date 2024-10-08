package com.shinhan.dongibuyeo.domain.account.service;

import com.shinhan.dongibuyeo.domain.account.client.AccountClient;
import com.shinhan.dongibuyeo.domain.account.dto.client.*;
import com.shinhan.dongibuyeo.domain.account.dto.request.DepositRequest;
import com.shinhan.dongibuyeo.domain.account.dto.request.MakeAccountRequest;
import com.shinhan.dongibuyeo.domain.account.dto.request.TransactionHistoryRequest;
import com.shinhan.dongibuyeo.domain.account.dto.request.TransferRequest;
import com.shinhan.dongibuyeo.domain.account.dto.response.*;
import com.shinhan.dongibuyeo.domain.account.entity.Account;
import com.shinhan.dongibuyeo.domain.account.exception.AccountNotFoundException;
import com.shinhan.dongibuyeo.domain.account.mapper.AccountMapper;
import com.shinhan.dongibuyeo.domain.account.repository.AccountRepository;
import com.shinhan.dongibuyeo.domain.alarm.service.NotificationService;
import com.shinhan.dongibuyeo.domain.member.entity.Member;
import com.shinhan.dongibuyeo.domain.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class AccountService {

    @Value("${shinhan.key}")
    private String apiKey;

    private final AccountRepository accountRepository;

    private final AccountClient accountClient;

    private final AccountMapper accountMapper;

    private final MemberService memberService;

    private final NotificationService notificationService;

    public AccountService(AccountRepository accountRepository, AccountClient accountClient, AccountMapper accountMapper, MemberService memberService, NotificationService notificationService) {
        this.accountRepository = accountRepository;
        this.accountClient = accountClient;
        this.accountMapper = accountMapper;
        this.memberService = memberService;
        this.notificationService = notificationService;
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
        Account account = accountRepository.save(accountMapper.toChallengeAccountEntity(shinhanAccount));
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

        if(member.getDeviceToken() != null && !member.getDeviceToken().isBlank()) {
            log.info("[sendNotification] deviceToken: {}", member.getDeviceToken());
            notificationService.sendNotification(member, "계좌 이체", "계좌 이체");
        }

        return transfer.getRec();
    }

    @Transactional
    public DepositResponse accountDeposit(DepositRequest depositRequest) {
        Member member = memberService.getMemberById(depositRequest.getMemberId());
        ShinhanDepositResponse deposit = accountClient.accountDeposit(accountMapper.toShinhanDepositRequest(depositRequest,apiKey,member));

        return deposit.getRec();
    }

    @Transactional
    public TransactionHistorys getMemberTransactionHistory(TransactionHistoryRequest request) {
        Member member = memberService.getMemberById(request.getMemberId());

        ShinhanTransactionHistoryResponse historys = accountClient.getTransactionHistory(
                accountMapper.toShinhanTransactionHistoryRequest(request,apiKey,member)
        );

        return historys.getRec();
    }

    @Transactional
    public void deleteAccountByAccountNo(String accountNo) {
        Account account = accountRepository.findByAccountNo(accountNo)
                .orElseThrow(() -> new AccountNotFoundException(accountNo));

        account.softDelete();
    }

    @Transactional
    public AccountDetailInfo getChallengeAccountByMemberId(UUID memberId) {
        log.info("[getChallengeAccountByMemberId] memberId: {}", memberId);
        Member member = memberService.getMemberById(memberId);
        String challengeAccountNo = member.getChallengeAccount().getAccountNo();
        log.info("getChallengeAccountByMemberId challengeAccountNo: {}", challengeAccountNo);
        return getAccountByAccountNo(memberId, challengeAccountNo);
    }
}
