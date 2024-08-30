package com.shinhan.dongibuyeo.domain.challenge.score.scheduler;

import com.shinhan.dongibuyeo.domain.alarm.service.NotificationService;
import com.shinhan.dongibuyeo.domain.challenge.entity.Challenge;
import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeStatus;
import com.shinhan.dongibuyeo.domain.challenge.repository.ChallengeRepository;
import com.shinhan.dongibuyeo.domain.challenge.service.ChallengeRewardService;
import com.shinhan.dongibuyeo.domain.challenge.service.DailyScoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@EnableScheduling
@Slf4j
public class ChallengeScheduler {

    private final NotificationService notificationService;
    private final ChallengeRepository challengeRepository;
    private final ChallengeRewardService challengeRewardService;
    private final DailyScoreService dailyScoreService;
    private final JobLauncher jobLauncher;
    private final Job job;

    public ChallengeScheduler(ChallengeRepository challengeRepository, ChallengeRewardService challengeRewardService, DailyScoreService dailyScoreService, NotificationService notificationService, JobLauncher jobLauncher, Job job) {
        this.challengeRepository = challengeRepository;
        this.challengeRewardService = challengeRewardService;
        this.dailyScoreService = dailyScoreService;
        this.notificationService = notificationService;
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    /**
     * 매일 자정 모든 챌린지 상태 변경 및 일일 점수 생성
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void updateChallengeStatusesAndCreateDailyScores() {
        LocalDate today = LocalDate.now();
        log.info("Starting daily challenge update and score creation for date: {}", today);

        challengeRepository.findAll().forEach(challenge -> {
            LocalDate now = LocalDate.now();
            LocalDate startDate = challenge.getStartDate();
            LocalDate endDate = challenge.getEndDate();

            if (startDate == null || endDate == null || now.isBefore(startDate)) {
                challenge.updateStatus(ChallengeStatus.SCHEDULED);

            } else if (challenge.getStatus() == ChallengeStatus.SCHEDULED){
                notificationService.sendNotificationMemberChallenges(challenge.getChallengeMembers(),"챌린지 시작","사전 신청하신 챌린지가 시작되었습니다!");
                challenge.updateStatus(ChallengeStatus.IN_PROGRESS);
            }

            // 진행 중인 챌린지 일일 점수 생성
            if (challenge.getStatus() == ChallengeStatus.IN_PROGRESS) {
                challenge.getChallengeMembers().forEach(memberChallenge -> {
                    try {
                        dailyScoreService.getOrCreateDailyScore(memberChallenge, today);
                    } catch (Exception e) {
                        log.error("Error creating daily score for challenge member: {}", memberChallenge.getId(), e);
                    }
                });
            }
        });
    }

    /**
     * 완료 챌린지 정산
     * 매일 새벽 4시 실행 (최종 피버타임 이후)
     */
//    @Scheduled(cron = "0 0 4 * * ?")
//    @Transactional
//    public void checkAndCompleteEndedChallenges() {
//        log.info("Starting to check for ended challenges");
//        LocalDate yesterday = LocalDate.now().minusDays(1);
//
//        List<Challenge> endedChallenges = challengeRepository.findChallengesByEndDate(yesterday);
//        for (Challenge challenge : endedChallenges) {
//            try {
//                log.info("Processing ended challenge: {}", challenge.getId());
//                challengeRewardService.processConsumptionChallengeRewards(challenge);
//                challenge.updateStatus(ChallengeStatus.COMPLETED);
//                notificationService.sendNotificationMemberChallenges(challenge.getChallengeMembers(),"챌린지 종료","참여하신 챌린지가 종료되었습니다! 결과를 확인해주세요!");
//                log.info("Challenge {} completed and rewards processed", challenge.getId());
//            } catch (Exception e) {
//                log.error("Error processing challenge {}: {}", challenge.getId(), e.getMessage(), e);
//            }
//        }
//        log.info("Finished checking for ended challenges");
//    }

    @Scheduled(cron = "0 0 4 * * ?")
    public void runCheckAndCompleteEndedChallengesJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        jobLauncher.run(
            job, new JobParametersBuilder().addLong("timeStamp", System.currentTimeMillis()).toJobParameters()
        );
    }

}
