package com.shinhan.dongibuyeo.domain.alarm.scheduler;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.shinhan.dongibuyeo.domain.alarm.service.FCMService;
import com.shinhan.dongibuyeo.domain.alarm.service.NotificationService;
import com.shinhan.dongibuyeo.domain.challenge.service.ChallengeService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class NotificationScheduler {
    private final NotificationService notificationService;
    private final ChallengeService challengeService;

    public NotificationScheduler(NotificationService notificationService, ChallengeService challengeService) {
        this.notificationService = notificationService;
        this.challengeService = challengeService;
    }

    @Scheduled(cron = "0 0 07 * * ?")
    public void pushCoffeeMorningFeverTimeNotification() throws FirebaseMessagingException {

    }

    @Scheduled(cron = "0 0 11 * * ?")
    public void pushCoffeeEveningFeverTimeNotification() throws FirebaseMessagingException {

    }

    @Scheduled(cron = "0 0 12 * * SAT")
    public void pushSaturdayDrinkFeverTimeNotification() throws FirebaseMessagingException {

    }

    @Scheduled(cron = "0 0 12 * * SUN")
    public void pushSundayDrinkFeverTimeNotification() throws FirebaseMessagingException {

    }

    @Scheduled(cron = "0 0 21 * * ?")
    public void pushDeliveryFeverTimeNotification() throws FirebaseMessagingException {

    }

    @Scheduled(cron = "0 0 22 * * ?")
    public void pushSavingNotification() throws FirebaseMessagingException {

    }


}
