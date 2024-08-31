package com.shinhan.dongibuyeo.domain.consume.service;

import com.shinhan.dongibuyeo.domain.account.client.AccountClient;
import com.shinhan.dongibuyeo.domain.account.dto.client.ShinhanTransactionHistoryResponse;
import com.shinhan.dongibuyeo.domain.account.dto.request.TransactionHistoryRequest;
import com.shinhan.dongibuyeo.domain.account.dto.response.TransactionHistory;
import com.shinhan.dongibuyeo.domain.account.mapper.AccountMapper;
import com.shinhan.dongibuyeo.domain.consume.dto.request.ConsumptionRequest;
import com.shinhan.dongibuyeo.domain.consume.dto.request.MakeConsumptionRequest;
import com.shinhan.dongibuyeo.domain.consume.dto.response.ConsumptionResponse;
import com.shinhan.dongibuyeo.domain.consume.entity.Consumption;
import com.shinhan.dongibuyeo.domain.member.entity.Member;
import com.shinhan.dongibuyeo.domain.member.service.MemberService;
import com.shinhan.dongibuyeo.global.entity.TransferType;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ConsumeService {
    private static final Logger log = LoggerFactory.getLogger(ConsumeService.class);
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
    public ConsumptionResponse getTotalConsumption(ConsumptionRequest request) {
        Member member = memberService.getMemberById(request.getHistory().getMemberId());
        log.info("[getTotalConsumption] member: {}", member.getId());
        AtomicLong result = new AtomicLong(0);

        ShinhanTransactionHistoryResponse response = accountClient.getTransactionHistory(
                accountMapper.toShinhanTransactionHistoryRequest(request.getHistory(),apiKey,member)
        );

        log.info("[getTotalConsumption] response: {}", response.getRec());
        response.getRec().getTransactions().addAll(
                getTransactionHistoryFromConsumption(member,request.getTransferType(),request.getHistory().getStartDate(),request.getHistory().getEndDate()).stream().map(this::toHistory).toList()
        );

        response.getRec().getTransactions().forEach(transaction -> {
            if(transaction.getTransactionSummary().startsWith(request.getTransferType().toString())) {
                result.addAndGet(Math.abs(transaction.getTransactionAfterBalance() - transaction.getTransactionBalance()));
            }
        });

        return new ConsumptionResponse(request.getTransferType(),result.get());
    }

    private TransactionHistory toHistory(Consumption consumption) {
        return new TransactionHistory(
                consumption.getTransactionUniqueNo(),
                consumption.getTransactionDate(),
                consumption.getTransactionTime(),
                consumption.getTransactionType(),
                consumption.getTransactionTypeName(),
                consumption.getTransactionAccountNo(),
                consumption.getTransactionBalance(),
                consumption.getTransactionAfterBalance(),
                consumption.getTransactionSummary(),
                consumption.getTransactionMemo()
        );
    }

    private List<Consumption> getTransactionHistoryFromConsumption(Member member, TransferType transferType, String startDate, String endDate) {
        List<Consumption> getConsumptions = new ArrayList<>();

        for(Consumption cur : member.getConsumptions()) {
            if(cur.getTransactionSummary().startsWith(transferType.toString()) && cur.getTransactionDate().compareTo(startDate) >= 0 && cur.getTransactionDate().compareTo(endDate) <= 0) {
                getConsumptions.add(cur);
            }
        }

        return getConsumptions;
    }

    @Transactional
    public List<TransactionHistory> getTypeHistory(ConsumptionRequest request) {
        Member member = memberService.getMemberById(request.getHistory().getMemberId());

        ShinhanTransactionHistoryResponse response = accountClient.getTransactionHistory(
                accountMapper.toShinhanTransactionHistoryRequest(request.getHistory(),apiKey,member)
        );

        response.getRec().getTransactions().addAll(
                getTransactionHistoryFromConsumption(member,request.getTransferType(),request.getHistory().getStartDate(),request.getHistory().getEndDate()).stream().map(this::toHistory).toList()
        );

        return response.getRec().getTransactions().stream().filter(x -> x.getTransactionSummary().startsWith(request.getTransferType().toString())).toList();
    }

    @Transactional
    public long getMembersTotalConsumption(UUID memberId, LocalDate startDate, LocalDate endDate, TransferType transferType) {
        Member member = memberService.getMemberById(memberId);

        AtomicLong totalConsumption = new AtomicLong(0L);
        member.getAccounts()
                .forEach(
                        account -> {

                            ConsumptionRequest request = new ConsumptionRequest(
                                    transferType,
                                    TransactionHistoryRequest.builder()
                                            .memberId(memberId)
                                            .accountNo(account.getAccountNo())
                                            .startDate(startDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                                            .endDate(endDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                                            .transactionType("D")
                                            .orderByType("DESC")
                                            .build()
                            );
                            totalConsumption.addAndGet(getTotalConsumption(request).getTotalConsumption());
                        }
                );

        return totalConsumption.get();
    }


    @Transactional
    public void addConsumption(MakeConsumptionRequest request) {
        Member member = memberService.getMemberById(request.getMemberId());

        member.getConsumptions().add(
                new Consumption(
                        member,
                        request.getTransactionUniqueNo(),
                        request.getTransactionDate(),
                        request.getTransactionTime(),
                        request.getTransactionType(),
                        request.getTransactionTypeName(),
                        request.getTransactionAccountNo(),
                        request.getTransactionBalance(),
                        request.getTransactionAfterBalance(),
                        request.getTransactionSummary(),
                        request.getTransactionMemo()
                )
        );
    }
}
