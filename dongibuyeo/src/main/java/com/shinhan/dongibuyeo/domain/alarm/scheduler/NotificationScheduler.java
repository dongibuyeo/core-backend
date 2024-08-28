package com.shinhan.dongibuyeo.domain.alarm.scheduler;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.shinhan.dongibuyeo.domain.alarm.service.FCMService;
import com.shinhan.dongibuyeo.domain.alarm.service.NotificationService;
import com.shinhan.dongibuyeo.domain.challenge.entity.Challenge;
import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeType;
import com.shinhan.dongibuyeo.domain.challenge.service.ChallengeService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NotificationScheduler {
    private final NotificationService notificationService;
    private final ChallengeService challengeService;

    public NotificationScheduler(NotificationService notificationService, ChallengeService challengeService) {
        this.notificationService = notificationService;
        this.challengeService = challengeService;
    }

    private void sendAllNotifications(List<Challenge> challenges, String title, String content) {
        challenges.stream().forEach(
                challenge -> {
                    challenge.getChallengeMembers()
                            .stream()
                            .forEach(member -> notificationService.sendNotification(member.getMember(),title,content));
                }
        );
    }

    @Scheduled(cron = "0 0 07 * * ?")
    public void pushCoffeeMorningFeverTimeNotification() throws FirebaseMessagingException {
        List<Challenge> challenges = challengeService.findAllChallengesByStatusAndDate(ChallengeType.CONSUMPTION_COFFEE, LocalDate.now());
        sendAllNotifications(challenges,"챌린지 피버 타임 [커피]","커피 챌린지 오전 피버 타임 (7시 부터 10시)");
    }

    @Scheduled(cron = "0 0 11 * * ?")
    public void pushCoffeeEveningFeverTimeNotification() throws FirebaseMessagingException {
        List<Challenge> challenges = challengeService.findAllChallengesByStatusAndDate(ChallengeType.CONSUMPTION_COFFEE, LocalDate.now());
        sendAllNotifications(challenges,"챌린지 피버 타임 [커피]","커피 챌린지 오후 피버 타임 (11시 부터 13시)");
    }

    @Scheduled(cron = "0 0 12 * * SAT")
    public void pushSaturdayDrinkFeverTimeNotification() throws FirebaseMessagingException {
        List<Challenge> challenges = challengeService.findAllChallengesByStatusAndDate(ChallengeType.CONSUMPTION_DRINK, LocalDate.now());
        sendAllNotifications(challenges,"챌린지 피버 타임 [음주]","토요일 하루 음주 대신 산책은 어떤가요?");
    }

    @Scheduled(cron = "0 0 12 * * SUN")
    public void pushSundayDrinkFeverTimeNotification() throws FirebaseMessagingException {
        List<Challenge> challenges = challengeService.findAllChallengesByStatusAndDate(ChallengeType.CONSUMPTION_DRINK, LocalDate.now());
        sendAllNotifications(challenges,"챌린지 피버 타임 [음주]","월요일을 위해 술 대신 음악 듣기는 어떤가요?");
    }

    @Scheduled(cron = "0 0 21 * * ?")
    public void pushDeliveryFeverTimeNotification() throws FirebaseMessagingException {
        List<Challenge> challenges = challengeService.findAllChallengesByStatusAndDate(ChallengeType.CONSUMPTION_DRINK, LocalDate.now());
        sendAllNotifications(challenges,"챌린지 피버 타임 [배달음식]","배달 음식을 참으면 건강과 지갑까지 1석 2조 ??? 완전 럭키비키잖아~");
    }

    @Scheduled(cron = "0 0 22 * * ?")
    public void pushSavingNotification() throws FirebaseMessagingException {
        List<Challenge> challenges = challengeService.findAllChallengesByStatusAndDate(ChallengeType.CONSUMPTION_DRINK, LocalDate.now());
        sendAllNotifications(challenges,"777 적금 납부 알림","다음날 6시 30분 777 적금 계좌에서 7000원이 출금될 예정입니다. 잔고를 확인해주세요!");
    }


}
