package com.shinhan.dongibuyeo.config;

import com.shinhan.dongibuyeo.domain.alarm.service.NotificationService;
import com.shinhan.dongibuyeo.domain.challenge.entity.Challenge;
import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeStatus;
import com.shinhan.dongibuyeo.domain.challenge.repository.ChallengeRepository;
import com.shinhan.dongibuyeo.domain.challenge.service.ChallengeRewardService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;

import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.List;



@Configuration
public class BatchConfig {

    private ChallengeRepository challengeRepository;
    private ChallengeRewardService challengeRewardService;
    private NotificationService notificationService;

    public BatchConfig(ChallengeRepository challengeRepository,ChallengeRewardService challengeRewardService,NotificationService notificationService) {
        this.challengeRepository = challengeRepository;
        this.challengeRewardService = challengeRewardService;
        this.notificationService = notificationService;
    }

    @Bean
    public Job testSimpleJob(PlatformTransactionManager transactionManager, JobRepository jobRepository) {
        return new JobBuilder("SETTLEMENT_JOB", jobRepository)
                .start(testSimpleStep(transactionManager, jobRepository))
                .build();
    }

    @Bean
    public Step testSimpleStep(PlatformTransactionManager transactionManager, JobRepository jobRepository) {
        return new StepBuilder("SETTLEMENT_STEP", jobRepository)
                .tasklet(checkAndCompleteEndedChallengesTasklet(),transactionManager)
                .build();
    }

    @Bean
    public Tasklet checkAndCompleteEndedChallengesTasklet() {
        return (contribution, chunkContext) -> {
            LocalDate yesterday = LocalDate.now().minusDays(1);
            List<Challenge> endedChallenges = challengeRepository.findChallengesByEndDate(yesterday);

            endedChallenges.stream()
                    .peek(challenge -> challengeRewardService.processConsumptionChallengeRewards(challenge))
                    .peek(challenge -> challenge.updateStatus(ChallengeStatus.COMPLETED))
                    .forEach(challenge -> notificationService.sendNotificationMemberChallenges(
                            challenge.getChallengeMembers(),
                            "챌린지 종료",
                            "참여하신 챌린지가 종료되었습니다! 결과를 확인해주세요!"
                    ));

            return RepeatStatus.FINISHED;
        };
    }

}
