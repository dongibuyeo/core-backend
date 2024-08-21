package com.shinhan.dongibuyeo.domain.account.mapper;

import com.shinhan.dongibuyeo.domain.account.dto.client.ShinhanMakeAccountRequest;
import com.shinhan.dongibuyeo.domain.account.dto.client.ShinhanMakeAccountResponse;
import com.shinhan.dongibuyeo.domain.account.dto.request.MakeAccountRequest;
import com.shinhan.dongibuyeo.domain.account.dto.response.AccountDetail;
import com.shinhan.dongibuyeo.domain.account.dto.response.AccountResponse;
import com.shinhan.dongibuyeo.domain.account.dto.response.Currency;
import com.shinhan.dongibuyeo.domain.account.entity.Account;
import com.shinhan.dongibuyeo.domain.member.entity.Member;
import com.shinhan.dongibuyeo.global.header.GlobalUserHeader;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    public ShinhanMakeAccountRequest toShinhanMakeAccountRequest(MakeAccountRequest request, Member member) {
        return new ShinhanMakeAccountRequest(
                new GlobalUserHeader("createDemandDepositAccount",member.getUserKey()),
                request.getAccountTypeUniqueNo()
        );
    }

    public Account toAccountEntity(ShinhanMakeAccountResponse request, Member member) {
        return new Account(
                member,
                request.getRec().getBankCode(),
                request.getRec().getAccountNo(),
                request.getRec().getCurrency().getCurrency(),
                request.getRec().getCurrency().getCurrencyName()
        );
    }

    public AccountResponse toAccountResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                new AccountDetail(
                        account.getBankCode(),
                        account.getAccountNo(),
                        new Currency(
                                account.getCurrency(),
                                account.getCurrencyName()
                        )
                )
        );
    }
}
