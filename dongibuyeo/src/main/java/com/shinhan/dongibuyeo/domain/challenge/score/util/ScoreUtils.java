package com.shinhan.dongibuyeo.domain.challenge.score.util;

import com.shinhan.dongibuyeo.domain.challenge.dto.response.AdditionalRewardResponse;

public class ScoreUtils {

    private ScoreUtils() {}

    /**
     * 추가 환급금 계산 메서드
     *
     * @param interestRate 챌린지 계좌 연이자
     * @param totalDeposit 총 예치금
     * @param leftDeposit 기본급(예치금 환불) 후 잔여 예치금
     * @param top10PercentMemberNum 상위 10% 해당 회원 수
     * @param lower90PercentMemberNum 하위 90% 해당 회원 수
     * @return 총상금, 상위 10% 회원의 단위(만원) 추가환급금, 하위 90% 회원의 단위 추가환급금
     */
    public static AdditionalRewardResponse calculateEstimatedAdditionalRewardPerUnit(
            double interestRate,
            Long totalDeposit,
            Long leftDeposit,
            int top10PercentMemberNum,
            int lower90PercentMemberNum) {

        // 총 상금: 잔여 금액 + (총상금 * 연이자/12)
        long totalReward = leftDeposit + (long) (totalDeposit * (interestRate / 12));

        // 상위 퍼센트 단위 상금 계산: (상금 * 0.5) / 해당 그룹 회원수
        long top10RewardPerUnit = (totalReward / 2) / top10PercentMemberNum;
        long lower90RewardPerUnit = (totalReward / 2) / lower90PercentMemberNum;

        // RewardResponseDTO 객체 생성 및 반환
        return AdditionalRewardResponse.builder()
                .totalReward(totalReward)
                .top10PercentRewardPerUnit(top10RewardPerUnit)
                .lower90PercentRewardPerUnit(lower90RewardPerUnit)
                .build();
    }
}
