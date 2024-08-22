package com.shinhan.dongibuyeo.domain.account.mapper;

import com.shinhan.dongibuyeo.domain.account.dto.client.*;
import com.shinhan.dongibuyeo.domain.account.dto.request.MakeAccountRequest;
import com.shinhan.dongibuyeo.domain.account.dto.request.TransferRequest;
import com.shinhan.dongibuyeo.domain.account.dto.response.*;
import com.shinhan.dongibuyeo.domain.account.entity.Account;
import com.shinhan.dongibuyeo.domain.account.entity.AccountType;
import com.shinhan.dongibuyeo.domain.member.entity.Member;
import com.shinhan.dongibuyeo.global.header.GlobalUserHeader;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    public ShinhanMakeAccountRequest toShinhanMakeAccountRequest(MakeAccountRequest request,String apiKey, Member member) {
        return new ShinhanMakeAccountRequest(
                new GlobalUserHeader("createDemandDepositAccount",apiKey,member.getUserKey()),
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

    public Account detailToPersonalAccountEntity(AccountDetailInfo info) {
        return new Account(
                info.getAccountNo(),
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
