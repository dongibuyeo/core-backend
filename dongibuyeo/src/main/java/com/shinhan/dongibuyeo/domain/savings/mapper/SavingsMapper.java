package com.shinhan.dongibuyeo.domain.savings.mapper;

import com.shinhan.dongibuyeo.domain.member.entity.Member;
import com.shinhan.dongibuyeo.domain.savings.dto.client.ShinhanGetUserSavingsRequest;
import com.shinhan.dongibuyeo.domain.savings.dto.client.ShinhanMakeSavingAccountRequest;
import com.shinhan.dongibuyeo.domain.savings.dto.client.ShinhanMakeSavingRequest;
import com.shinhan.dongibuyeo.domain.savings.dto.client.ShinhanSavingRequest;
import com.shinhan.dongibuyeo.domain.savings.dto.request.MakeSavingAccountRequest;
import com.shinhan.dongibuyeo.domain.savings.dto.request.SavingProductRequest;
import com.shinhan.dongibuyeo.global.header.GlobalAdminHeader;
import com.shinhan.dongibuyeo.global.header.GlobalUserHeader;
import org.springframework.stereotype.Component;

@Component
public class SavingsMapper {

    public ShinhanMakeSavingRequest toShinhanMakeSavingRequest(SavingProductRequest request, String apiKey) {
        return new ShinhanMakeSavingRequest(
                new GlobalAdminHeader("createProduct", apiKey),
                request.getBankCode(),
                request.getAccountName(),
                request.getAccountDescription(),
                request.getMinSubscriptionBalance(),
                request.getSubscriptionPeriod(),
                request.getMaxSubscriptionBalance(),
                request.getInterestRate(),
                request.getRateDescription()
        );
    }

    public ShinhanMakeSavingAccountRequest toShinhanMakeSavingAccountRequest(MakeSavingAccountRequest request, String apiKey, Member member) {
        return new ShinhanMakeSavingAccountRequest(
                new GlobalUserHeader("createAccount", apiKey, member.getUserKey()),
                request.getWithdrawalAccountNo(),
                request.getAccountTypeUniqueNo(),
                request.getDepositBalance()
        );
    }

    public ShinhanSavingRequest toShinhanSavingRequest(String apiName, String apiKey, Member member, String accountNo) {
        return new ShinhanSavingRequest(
                new GlobalUserHeader(apiName, apiKey, member.getUserKey()),
                accountNo
        );
    }

    public ShinhanGetUserSavingsRequest toShinhanGetMemberSavingsRequest(String apiKey, String userKey) {
        return new ShinhanGetUserSavingsRequest(
                new GlobalUserHeader("inquireAccountList",apiKey,userKey)
        );
    }
}
