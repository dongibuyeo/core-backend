package com.shinhan.dongibuyeo.domain.challenge.score.aop;

import com.shinhan.dongibuyeo.domain.account.dto.request.TransferRequest;
import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeType;
import com.shinhan.dongibuyeo.domain.challenge.entity.DailyScore;
import com.shinhan.dongibuyeo.domain.challenge.entity.MemberChallenge;
import com.shinhan.dongibuyeo.domain.challenge.entity.ScoreDetail;
import com.shinhan.dongibuyeo.domain.challenge.service.MemberChallengeService;
import com.shinhan.dongibuyeo.domain.challenge.service.DailyScoreService;
import com.shinhan.dongibuyeo.domain.quiz.dto.request.QuizSolveRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Aspect
@Component
public class ScoreAspect {

    private final MemberChallengeService memberChallengeService;
    private final DailyScoreService dailyScoreService;

    public ScoreAspect(MemberChallengeService memberChallengeService, DailyScoreService dailyScoreService) {
        this.memberChallengeService = memberChallengeService;
        this.dailyScoreService = dailyScoreService;
    }

    /**
     * 소비내역을 추적해 챌린지 점수 차감
     */
    @Transactional
    @AfterReturning("execution(* com.shinhan.dongibuyeo.*.controller.AccountController.accountTransfer(..))")
    public void afterTransfer(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        TransferRequest request = (TransferRequest) args[0];

        String challengeType = "CONSUMPTION_" + request.getTransferType();

        List<MemberChallenge> memberChallenges = memberChallengeService.findByMemberId(request.getMemberId());
        Optional<MemberChallenge> correspondChallenge = memberChallenges.stream()
                .filter(memberChallenge -> memberChallenge.getChallenge().getType().toString().equals(challengeType))
                .findAny();

        if (correspondChallenge.isPresent()) {
            MemberChallenge memberChallenge = correspondChallenge.get();
            LocalDate today = LocalDate.now();

            DailyScore todayScore = dailyScoreService.getOrCreateDailyScore(memberChallenge, today);

            int currentScore = todayScore.getTotalScore();
            ScoreDetail scoreDetail = new ScoreDetail(challengeType, -5, currentScore - 5);
            todayScore.updateScoreDetails(scoreDetail);
        }
    }

    /**
     * 퀴즈 정답내역을 추적해 챌린지 점수 추가
     */
    @Transactional
    @AfterReturning(pointcut = "execution(* com.shinhan.dongibuyeo.*.controller.QuizController.solveQuiz(..))", returning = "result")
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
        todayScore.updateScoreDetails(scoreDetail);
    }
}
