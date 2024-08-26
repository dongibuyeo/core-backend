package com.shinhan.dongibuyeo.domain.savings.service;

import com.shinhan.dongibuyeo.domain.account.entity.Account;
import com.shinhan.dongibuyeo.domain.account.mapper.AccountMapper;
import com.shinhan.dongibuyeo.domain.account.repository.AccountRepository;
import com.shinhan.dongibuyeo.domain.member.entity.Member;
import com.shinhan.dongibuyeo.domain.member.service.MemberService;
import com.shinhan.dongibuyeo.domain.savings.client.SavingsClient;
import com.shinhan.dongibuyeo.domain.savings.dto.client.ShinhanGetSavingsRequest;
import com.shinhan.dongibuyeo.domain.savings.dto.client.ShinhanMakeSavingAccountResponse;
import com.shinhan.dongibuyeo.domain.savings.dto.request.MakeSavingAccountRequest;
import com.shinhan.dongibuyeo.domain.savings.dto.request.SavingProductRequest;
import com.shinhan.dongibuyeo.domain.savings.dto.response.DeleteSavingInfo;
import com.shinhan.dongibuyeo.domain.savings.dto.response.SavingAccountInfo;
import com.shinhan.dongibuyeo.domain.savings.dto.response.SavingInfo;
import com.shinhan.dongibuyeo.domain.savings.dto.response.SavingPaymentInfo;
import com.shinhan.dongibuyeo.domain.savings.mapper.SavingsMapper;
import com.shinhan.dongibuyeo.global.header.GlobalAdminHeader;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SavingsService {

    private final SavingsClient savingsClient;
    @Value("${shinhan.key}")
    private String apiKey;

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    private final MemberService memberService;
    private final SavingsMapper savingsMapper;

    public SavingsService(AccountRepository accountRepository, AccountMapper accountMapper, MemberService memberService, SavingsMapper savingsMapper, SavingsClient savingsClient) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.memberService = memberService;
        this.savingsMapper = savingsMapper;
        this.savingsClient = savingsClient;
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
        return savingsClient.deleteSavingAccount(
                savingsMapper.toShinhanSavingRequest("inquireEarlyTerminationInterest", apiKey, member, accountNo))
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
}
