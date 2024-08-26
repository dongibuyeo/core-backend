package com.shinhan.dongibuyeo.domain.challenge.strategy;

import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeType;
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