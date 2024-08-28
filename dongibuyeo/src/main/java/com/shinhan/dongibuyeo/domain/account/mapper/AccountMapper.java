package com.shinhan.dongibuyeo.domain.account.mapper;

import com.shinhan.dongibuyeo.domain.account.dto.client.*;
import com.shinhan.dongibuyeo.domain.account.dto.request.DepositRequest;
import com.shinhan.dongibuyeo.domain.account.dto.request.MakeAccountRequest;
import com.shinhan.dongibuyeo.domain.account.dto.request.TransactionHistoryRequest;
import com.shinhan.dongibuyeo.domain.account.dto.request.TransferRequest;
import com.shinhan.dongibuyeo.domain.account.dto.response.AccountDetailInfo;
import com.shinhan.dongibuyeo.domain.account.dto.response.MakeAccountResponse;
import com.shinhan.dongibuyeo.domain.account.entity.Account;
import com.shinhan.dongibuyeo.domain.account.entity.AccountType;
import com.shinhan.dongibuyeo.domain.member.entity.Member;
import com.shinhan.dongibuyeo.domain.savings.dto.client.ShinhanMakeSavingAccountResponse;
import com.shinhan.dongibuyeo.domain.savings.dto.response.SavingAccountsDetail;
import com.shinhan.dongibuyeo.global.header.GlobalUserHeader;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    public ShinhanMakeAccountRequest toShinhanMakeAccountRequest(MakeAccountRequest request, String apiKey, Member member) {
        return new ShinhanMakeAccountRequest(
                new GlobalUserHeader("createDemandDepositAccount", apiKey, member.getUserKey()),
                request.getAccountTypeUniqueNo()
        );
    }

    public ShinhanGetAccountsRequest toShinhanGetAccountsRequest(String apiKey, String userKey) {
        return new ShinhanGetAccountsRequest(
                new GlobalUserHeader("inquireDemandDepositAccountList",apiKey,userKey)
        );
    }

    public ShinhanGetAccountRequest toShinhanGetAccountRequest(String apiKey, String userKey, String accountNo) {
        return new ShinhanGetAccountRequest(
                new GlobalUserHeader("inquireDemandDepositAccount",apiKey, userKey),
                accountNo
        );
    }

    public ShinhanTransferRequest toShinhanTransferRequest(TransferRequest request, String apiKey, Member member) {
        return new ShinhanTransferRequest(
                new GlobalUserHeader("updateDemandDepositAccountTransfer",apiKey, member.getUserKey()),
                request.getDepositAccountNo(),
                request.getTransferType().toString()+"입금",
                request.getTransactionBalance(),
                request.getWithdrawalAccountNo(),
                request.getTransferType().toString()+"출금"
        );
    }

    public ShinhanDepositRequest toShinhanDepositRequest(DepositRequest request, String apiKey, Member member) {
        return new ShinhanDepositRequest(
                new GlobalUserHeader("updateDemandDepositAccountDeposit",apiKey, member.getUserKey()),
                request.getAccountNo(),
                request.getTransactionBalance(),
                "입금"
        );
    }

    public ShinhanTransactionHistoryRequest toShinhanTransactionHistoryRequest(TransactionHistoryRequest request, String apiKey, Member member) {
        return new ShinhanTransactionHistoryRequest(
                new GlobalUserHeader("inquireTransactionHistoryList",apiKey, member.getUserKey()),
                request.getAccountNo(),
                request.getStartDate(),
                request.getEndDate(),
                request.getTransactionType(),
                request.getOrderByType()
        );
    }

    public Account detailToPersonalAccountEntity(AccountDetailInfo info) {
        return new Account(
                info.getAccountNo(),
                AccountType.PRIVATE
        );
    }

    public Account detailToPersonalAccountEntity(SavingAccountsDetail accountsDetail) {
        return new Account(
                accountsDetail.getAccountNo(),
                AccountType.PRIVATE
        );
    }

    public Account detailToChallengeAccountEntity(AccountDetailInfo info) {
        return new Account(
                info.getAccountNo(),
                AccountType.CHALLENGE
        );
    }

    public Account toPersonalAccountEntity(ShinhanMakeAccountResponse request) {
        return new Account(
                request.getRec().getAccountNo(),
                AccountType.PRIVATE
        );
    }

    public Account toPersonalAccountEntity(ShinhanMakeSavingAccountResponse response) {
        return new Account(
                response.getRec().getAccountNo(),
                AccountType.PRIVATE
        );
    }

    public Account toChallengeAccountEntity(ShinhanMakeAccountResponse request) {
        return new Account(
                request.getRec().getAccountNo(),
                AccountType.CHALLENGE
        );
    }

    public MakeAccountResponse toAccountResponse(Account account) {
        return new MakeAccountResponse(
                account.getId(),
                account.getAccountNo(),
                account.getAccountType()
        );
    }
}