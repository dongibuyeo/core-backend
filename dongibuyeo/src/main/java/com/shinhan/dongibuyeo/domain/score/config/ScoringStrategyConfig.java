package com.shinhan.dongibuyeo.domain.score.config;

import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeType;
import com.shinhan.dongibuyeo.domain.score.strategy.ConsumptionCoffeeStrategy;
import com.shinhan.dongibuyeo.domain.score.strategy.ConsumptionDeliveryStrategy;
import com.shinhan.dongibuyeo.domain.score.strategy.ConsumptionDrinkStrategy;
import com.shinhan.dongibuyeo.domain.score.strategy.ScoringStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.Map;

@Configuration
public class ScoringStrategyConfig {

    @Bean
    public Map<ChallengeType, ScoringStrategy> scoringStrategies(
            ConsumptionCoffeeStrategy coffeeStrategy,
            ConsumptionDrinkStrategy drinkStrategy,
            ConsumptionDeliveryStrategy deliveryStrategy) {
        Map<ChallengeType, ScoringStrategy> strategies = new EnumMap<>(ChallengeType.class);
        strategies.put(ChallengeType.CONSUMPTION_COFFEE, coffeeStrategy);
        strategies.put(ChallengeType.CONSUMPTION_DRINK, drinkStrategy);
        strategies.put(ChallengeType.CONSUMPTION_DELIVERY, deliveryStrategy);
        return strategies;
    }
}