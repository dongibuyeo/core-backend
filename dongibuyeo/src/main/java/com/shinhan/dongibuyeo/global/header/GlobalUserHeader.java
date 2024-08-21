package com.shinhan.dongibuyeo.global.header;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GlobalUserHeader {
    private String apiName;
    private String transmissionDate;
    private String transmissionTime;
    private String institutionCode;
    private String fintechAppNo;
    private String apiServiceCode;
    private String institutionTransactionUniqueNo;
    private String apiKey;
    private String userKey;

    public GlobalUserHeader(String apiName, String apiKey, String userKey) {
        this.apiName = apiName;
        setTransmissionDateAndTime();
        this.institutionCode = "00100";
        this.fintechAppNo = "001";
        this.apiServiceCode = apiName;
        makeUniqueTransactionCode();
        this.apiKey = apiKey;
        this.userKey = userKey;
    }

    private void setTransmissionDateAndTime() {
        LocalDateTime now = LocalDateTime.now();
        this.transmissionDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        this.transmissionTime = now.format(DateTimeFormatter.ofPattern("HHmmss"));
    }

    private void makeUniqueTransactionCode() {
        StringBuilder uniqueCode = new StringBuilder();
        uniqueCode.append(transmissionDate);
        uniqueCode.append(transmissionTime);
        String randomCode = String.valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));
        uniqueCode.append(randomCode);

        this.institutionTransactionUniqueNo = uniqueCode.toString();
    }
}
