package com.shinhan.dongibuyeo.global.header;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GlobalAdminHeader {
    private String apiName;
    private String transmissionDate;
    private String transmissionTime;
    private String institutionCode;
    private String fintechAppNo;
    private String apiServiceCode;
    private String institutionTransactionUniqueNo;
    private String apiKey;

    public GlobalAdminHeader(String apiName, String apiKey) {
        this.apiName = apiName;
        this.apiKey = apiKey;
        setTransmissionDateAndTime();
        this.institutionCode = "00100";
        this.fintechAppNo = "001";
        this.apiServiceCode = apiName;
        makeUniqueTransactionCode();
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
