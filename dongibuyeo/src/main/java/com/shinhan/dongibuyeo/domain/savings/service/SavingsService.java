package com.shinhan.dongibuyeo.domain.savings.service;

import com.shinhan.dongibuyeo.domain.account.entity.Account;
import com.shinhan.dongibuyeo.domain.account.mapper.AccountMapper;
import com.shinhan.dongibuyeo.domain.account.repository.AccountRepository;
import com.shinhan.dongibuyeo.domain.account.service.AccountService;
import com.shinhan.dongibuyeo.domain.member.entity.Member;
import com.shinhan.dongibuyeo.domain.member.service.MemberService;
import com.shinhan.dongibuyeo.domain.savings.client.SavingsClient;
import com.shinhan.dongibuyeo.domain.savings.dto.client.ShinhanGetSavingAccountsResponse;
import com.shinhan.dongibuyeo.domain.savings.dto.client.ShinhanGetSavingsRequest;
import com.shinhan.dongibuyeo.domain.savings.dto.client.ShinhanMakeSavingAccountResponse;
import com.shinhan.dongibuyeo.domain.savings.dto.request.MakeSavingAccountRequest;
import com.shinhan.dongibuyeo.domain.savings.dto.request.MakeSevenSavingAccountRequest;
import com.shinhan.dongibuyeo.domain.savings.dto.request.SavingProductRequest;
import com.shinhan.dongibuyeo.domain.savings.dto.response.*;
import com.shinhan.dongibuyeo.domain.savings.exception.SavingAccountAlreadyExistsException;
import com.shinhan.dongibuyeo.domain.savings.exception.SavingProductNotFoundException;
import com.shinhan.dongibuyeo.domain.savings.mapper.SavingsMapper;
import com.shinhan.dongibuyeo.global.header.GlobalAdminHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SavingsService {

    @Value("${shinhan.key}")
    private String apiKey;

    private final SavingsClient savingsClient;

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;
    private final SavingsMapper savingsMapper;

    private final AccountService accountService;
    private final MemberService memberService;

    public SavingsService(AccountRepository accountRepository, AccountMapper accountMapper, MemberService memberService, SavingsMapper savingsMapper, SavingsClient savingsClient, AccountService accountService) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.memberService = memberService;
        this.savingsMapper = savingsMapper;
        this.savingsClient = savingsClient;
        this.accountService = accountService;
    }

    @Transactional
    public SavingInfo makeSavingProduct(SavingProductRequest request) {
        return savingsClient.makeSavingProduct(
                        savingsMapper.toShinhanMakeSavingRequest(request, apiKey))
                .getRec();
    }

    @Transactional
    public SavingAccountInfo makeSavingAccount(MakeSavingAccountRequest request) {
        Member member = memberService.getMemberById(request.getMemberId());
        ShinhanMakeSavingAccountResponse shinhanAccount = savingsClient.makeSavingAccount(savingsMapper.toShinhanMakeSavingAccountRequest(request, apiKey, member));

        Account account = accountRepository.save(accountMapper.toPersonalAccountEntity(shinhanAccount));
        account.updateMember(member);
        return shinhanAccount.getRec();
    }

    @Transactional
    public DeleteSavingInfo deleteSavingAccounts(UUID memberId, String accountNo) {
        Member member = memberService.getMemberById(memberId);

        accountService.deleteAccountByAccountNo(accountNo);
        return savingsClient.deleteSavingAccount(
                        savingsMapper.toShinhanSavingRequest("deleteAccount", apiKey, member, accountNo))
                .getRec();
    }

    @Transactional
    public List<SavingPaymentInfo> getSavingPayment(UUID memberId, String accountNo) {
        Member member = memberService.getMemberById(memberId);

        return savingsClient.getSavingPayment(
                        savingsMapper.toShinhanSavingRequest("inquirePayment", apiKey, member, accountNo))
                .getRec();
    }

    public List<SavingInfo> getSavingProducts() {
        return savingsClient.getAllSavings(
                new ShinhanGetSavingsRequest(
                        new GlobalAdminHeader("inquireSavingsProducts", apiKey)
                )
        ).getRec();
    }

    @Transactional
    public List<SavingAccountsDetail> getAllSavingsByMemberId(UUID memberId) {
        Member member = memberService.getMemberById(memberId);
        ShinhanGetSavingAccountsResponse savingAccounts = savingsClient.getAllSavingAccountsByMemberId(
                savingsMapper.toShinhanGetMemberSavingsRequest(apiKey, member.getUserKey()));

        savingAccounts.getRec().getSavingDetails().forEach(
                x -> member.getAccounts().add(accountMapper.detailToPersonalAccountEntity(x))
        );
        return savingAccounts.getRec().getSavingDetails();
    }

    @Transactional
    public SavingAccountInfo makeSevenSavingAccount(MakeSevenSavingAccountRequest request) {
        String accountName = request.getChallengeType() + request.getStartDate().substring(2);
        UUID memberId = request.getMemberId();
        String withdrawalAccountNo = request.getWithdrawalAccountNo();
        Long depositBalance = request.getDepositBalance();

        SavingInfo sevenSavingProduct = findSavingInfoByAccountName(accountName);


        Optional<SavingAccountsDetail> memberSavingAccount = findMemberSavingAccountByAccountName(memberId, accountName);
        if (memberSavingAccount.isPresent()) {
            throw new SavingAccountAlreadyExistsException(accountName);
        }

        // 해당 상품 가입
        return makeSavingAccount(
                new MakeSavingAccountRequest(
                        memberId,
                        withdrawalAccountNo,
                        sevenSavingProduct.getAccountTypeUniqueNo(),
                        depositBalance
                )
        );
    }

    @Transactional(readOnly = true)
    public Optional<SavingAccountsDetail> findMemberSavingAccountByAccountName(UUID memberId, String accountName) {
        return getAllSavingsByMemberId(memberId)
                .stream()
                .filter(savingInfo -> savingInfo.getAccountName().equals(accountName))
                .findFirst();
    }

    public SavingInfo findSavingInfoByAccountName(String accountName) {
        // 계좌명이 일치하는 적금 상품 찾기
        return getSavingProducts()
                .stream()
                .filter(savingInfo -> savingInfo.getAccountName().equals(accountName) && savingInfo.getMinSubscriptionBalance() <= 7000)
                .findFirst()
                .orElseThrow(() -> new SavingProductNotFoundException(accountName));
    }
}
