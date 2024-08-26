package com.shinhan.dongibuyeo.domain.challenge.score.scheduler;

import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeType;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class FeverTimeChecker {

    public static boolean isFeverTime(ChallengeType type, LocalDateTime dateTime) {
        switch (type) {
            case CONSUMPTION_COFFEE:
                return isCoffeeFeverTime(dateTime);
            case CONSUMPTION_DRINK:
                return isAlcoholFeverTime(dateTime);
            case CONSUMPTION_DELIVERY:
                return isDeliveryFeverTime(dateTime);
            default:
                return false;
        }
    }

    private static boolean isCoffeeFeverTime(LocalDateTime dateTime) {
        int hour = dateTime.getHour();
        return (hour >= 7 && hour < 10) || (hour >= 11 && hour < 14);
    }

    private static boolean isAlcoholFeverTime(LocalDateTime dateTime) {
        DayOfWeek day = dateTime.getDayOfWeek();
        return day == DayOfWeek.FRIDAY || day == DayOfWeek.SATURDAY;
    }

    private static boolean isDeliveryFeverTime(LocalDateTime dateTime) {
        int hour = dateTime.getHour();
        return hour >= 21 || hour < 2;
    }
}
