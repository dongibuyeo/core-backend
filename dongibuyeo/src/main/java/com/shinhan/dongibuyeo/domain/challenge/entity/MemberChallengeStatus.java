package com.shinhan.dongibuyeo.domain.challenge.entity;

public enum MemberChallengeStatus {
    BEFORE_CALCULATION,       // 환급액 계산 전
    CALCULATED,               // 환급액 계산 완료(환급 전)
    REWARDED                  // 환급 완료
}