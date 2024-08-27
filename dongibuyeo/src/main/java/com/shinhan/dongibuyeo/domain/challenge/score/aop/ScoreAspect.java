package com.shinhan.dongibuyeo.domain.challenge.score.aop;

import com.shinhan.dongibuyeo.domain.account.dto.request.TransferRequest;
import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeType;
import com.shinhan.dongibuyeo.domain.challenge.entity.DailyScore;
import com.shinhan.dongibuyeo.domain.challenge.entity.MemberChallenge;
import com.shinhan.dongibuyeo.domain.challenge.entity.ScoreDetail;
import com.shinhan.dongibuyeo.domain.challenge.repository.DailyScoreRepository;
import com.shinhan.dongibuyeo.domain.challenge.service.DailyScoreService;
import com.shinhan.dongibuyeo.domain.challenge.service.MemberChallengeService;
import com.shinhan.dongibuyeo.domain.quiz.dto.request.QuizSolveRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Aspect
@Component
public class ScoreAspect {

    private final MemberChallengeService memberChallengeService;
    private final DailyScoreService dailyScoreService;
    private final DailyScoreRepository dailyScoreRepository;

    public ScoreAspect(MemberChallengeService memberChallengeService, DailyScoreService dailyScoreService, DailyScoreRepository dailyScoreRepository) {
        this.memberChallengeService = memberChallengeService;
        this.dailyScoreService = dailyScoreService;
        this.dailyScoreRepository = dailyScoreRepository;
    }

    /**
     * 소비내역을 추적해 챌린지 점수 차감
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @AfterReturning("execution(* com.shinhan.dongibuyeo.domain.account.controller.AccountController.accountTransfer(..))")
    public void afterTransfer(JoinPoint joinPoint) {
        log.info("[afterTransfer] 소비 내역 추적 시작");
        try {
            Object[] args = joinPoint.getArgs();
            TransferRequest request = (TransferRequest) args[0];

            String challengeType = "CONSUMPTION_" + request.getTransferType();
            log.info("Challenge Type: {}", challengeType);

            List<MemberChallenge> memberChallenges = memberChallengeService.findByMemberId(request.getMemberId());
            log.info("Found {} member challenges", memberChallenges.size());

            Optional<MemberChallenge> correspondChallenge = memberChallenges.stream()
                    .filter(memberChallenge -> memberChallenge.getChallenge().getType().toString().equals(challengeType))
                    .findAny();

            if (correspondChallenge.isPresent()) {
                MemberChallenge memberChallenge = correspondChallenge.get();
                log.info("Corresponding challenge found: {}", memberChallenge.getId());

                LocalDate today = LocalDate.now();
                dailyScoreService.updateDailyScore(memberChallenge, today, challengeType, -5);
            }

        } catch (Exception e) {
            log.error("Error in afterTransfer method", e);
        }
        log.info("[afterTransfer] 소비 내역 추적 종료");
    }

    /**
     * 퀴즈 정답내역을 추적해 챌린지 점수 추가
     */
    @Transactional
    @AfterReturning(pointcut = "execution(* com.shinhan.dongibuyeo.domain.quiz.controller.QuizController.solveQuiz(..))", returning = "result")
    public void afterSolveQuiz(JoinPoint joinPoint, Object result) {
        Object[] args = joinPoint.getArgs();
        QuizSolveRequest request = (QuizSolveRequest) args[0];

        LocalDate today = LocalDate.now();

        MemberChallenge memberChallenge = memberChallengeService.findByMemberIdAndChallengeType(
                request.getMemberId(),
                ChallengeType.QUIZ_SOLBEING);

        DailyScore todayScore = dailyScoreService.getOrCreateDailyScore(memberChallenge, today);

        int currentScore = todayScore.getTotalScore();
        ScoreDetail scoreDetail = new ScoreDetail("SOLVE_QUIZ", +5, currentScore + 5);
        todayScore.addScoreDetail(scoreDetail);
        memberChallenge.addDailyScore(todayScore);
    }
}
