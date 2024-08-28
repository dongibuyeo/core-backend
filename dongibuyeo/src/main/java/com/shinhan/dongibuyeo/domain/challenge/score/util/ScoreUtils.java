package com.shinhan.dongibuyeo.domain.challenge.score.util;

import com.shinhan.dongibuyeo.domain.challenge.dto.response.ChallengeResultResponse;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ScoreUtils {

    @Value("${shinhan.challenge.interest}")
    private String interestRate;

    /**
     * 추가 환급금 계산 메서드
     *
     * @param totalFund 총기금
     * @param totalParticipants 총 참여자수
     * @param excessAchievementRate 실패한 사람들의 초과 달성률
     * @param successRate 성공률
     * @return
     */
    public static ChallengeResultResponse calculateEstimatedReward(
            Long totalFund,
            int totalParticipants,
            double rewardDivisionFactor,
            double excessAchievementRate,
            double successRate
    ) {

        // 상수로 유지되는 값들
        double rewardDivisionRatio = 0.18;     // 상금 나누기 비율

        // BigDecimal을 사용하여 정밀도 유지
        BigDecimal fund = BigDecimal.valueOf(totalFund);
        BigDecimal divisionRatio = BigDecimal.valueOf(rewardDivisionRatio);
        BigDecimal excessRate = BigDecimal.valueOf(excessAchievementRate);
        BigDecimal interestRate = BigDecimal.valueOf(interestRate);
        BigDecimal divisionFactor = BigDecimal.valueOf(rewardDivisionFactor);
        BigDecimal topPercent = BigDecimal.valueOf(topPercentage);
        BigDecimal lowerPercent = BigDecimal.valueOf(lowerPercentage);
        BigDecimal successRateBD = BigDecimal.valueOf(successRate);
        BigDecimal participantsBD = BigDecimal.valueOf(totalParticipants);

        // 총 상금 계산: (총기금 * 0.18 * 0.2) + (총기금 * (0.04 / 12))
        BigDecimal totalReward = fund.multiply(divisionRatio)
                .multiply(excessRate)
                .add(fund.multiply(interestRate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP)));

        // 상위 퍼센트 단위 상금 계산: (상금 * 0.5) / (총참여자수 * 0.82 * 0.1)
        BigDecimal topRewardPerUnit = totalReward.multiply(divisionFactor)
                .divide(participantsBD.multiply(successRateBD).multiply(topPercent), 2, RoundingMode.HALF_UP);

        // 하위 퍼센트 단위 상금 계산: (상금 * 0.5) / (총참여자수 * 0.82 * 0.9)
        BigDecimal lowerRewardPerUnit = totalReward.multiply(divisionFactor)
                .divide(participantsBD.multiply(successRateBD).multiply(lowerPercent), 2, RoundingMode.HALF_UP);

        // RewardResponseDTO 객체 생성 및 반환
        return ChallengeResultResponse.builder()
                .totalReward(totalReward.setScale(2, RoundingMode.HALF_UP).doubleValue())
                .top10PercentRewardPerUnit(topRewardPerUnit.doubleValue())
                .lower90PercentRewardPerUnit(lowerRewardPerUnit.doubleValue())
                .build();
    }
}
