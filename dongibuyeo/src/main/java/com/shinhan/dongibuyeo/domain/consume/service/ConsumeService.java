package com.shinhan.dongibuyeo.domain.consume.service;

import com.shinhan.dongibuyeo.domain.account.client.AccountClient;
import com.shinhan.dongibuyeo.domain.account.dto.client.ShinhanTransactionHistoryResponse;
import com.shinhan.dongibuyeo.domain.account.dto.response.TransactionHistory;
import com.shinhan.dongibuyeo.domain.account.mapper.AccountMapper;
import com.shinhan.dongibuyeo.domain.consume.dto.request.ConsumtionRequest;
import com.shinhan.dongibuyeo.domain.consume.dto.response.ConsumtionResponse;
import com.shinhan.dongibuyeo.domain.member.entity.Member;
import com.shinhan.dongibuyeo.domain.member.service.MemberService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ConsumeService {
    @Value("${shinhan.key}")
    private String apiKey;
    private AccountClient accountClient;
    private AccountMapper accountMapper;
    private MemberService memberService;

    public ConsumeService(AccountClient accountClient, AccountMapper accountMapper, MemberService memberService) {
        this.accountClient = accountClient;
        this.accountMapper = accountMapper;
        this.memberService = memberService;
    }

    @Transactional
    public ConsumtionResponse getTotalConsumtion(ConsumtionRequest request) {
        Member member = memberService.getMemberById(request.getHistory().getMemberId());
        AtomicLong result = new AtomicLong(0);

        ShinhanTransactionHistoryResponse response = accountClient.getTransactionHistory(
                accountMapper.toShinhanTransactionHistoryRequest(request.getHistory(),apiKey,member)
        );

        response.getRec().getTransactions().forEach(transaction -> {
            if(transaction.getTransactionSummary().startsWith(request.getTransferType().toString())) {
                result.addAndGet(transaction.getTransactionBalance());
            }
        });

        return new ConsumtionResponse(request.getTransferType(),result.get());
    }

    @Transactional
    public List<TransactionHistory> getTypeHistory(ConsumtionRequest request) {
        Member member = memberService.getMemberById(request.getHistory().getMemberId());

        ShinhanTransactionHistoryResponse response = accountClient.getTransactionHistory(
                accountMapper.toShinhanTransactionHistoryRequest(request.getHistory(),apiKey,member)
        );

        return response.getRec().getTransactions().stream().filter(x -> x.getTransactionSummary().startsWith(request.getTransferType().toString())).toList();
    }

}
