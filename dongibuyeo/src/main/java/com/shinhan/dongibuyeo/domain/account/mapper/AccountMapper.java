package com.shinhan.dongibuyeo.domain.account.mapper;

import com.shinhan.dongibuyeo.domain.account.dto.client.ShinhanGetAccountsRequest;
import com.shinhan.dongibuyeo.domain.account.dto.client.ShinhanMakeAccountRequest;
import com.shinhan.dongibuyeo.domain.account.dto.client.ShinhanMakeAccountResponse;
import com.shinhan.dongibuyeo.domain.account.dto.request.MakeAccountRequest;
import com.shinhan.dongibuyeo.domain.account.dto.response.AccountDetailInfo;
import com.shinhan.dongibuyeo.domain.account.dto.response.AccountInfo;
import com.shinhan.dongibuyeo.domain.account.dto.response.MakeAccountResponse;
import com.shinhan.dongibuyeo.domain.account.dto.response.Currency;
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
