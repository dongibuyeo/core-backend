package com.shinhan.dongibuyeo.domain.quiz.controller;

import com.shinhan.dongibuyeo.domain.quiz.dto.request.QuizSolveRequest;
import com.shinhan.dongibuyeo.domain.quiz.dto.response.QuizResponse;
import com.shinhan.dongibuyeo.domain.quiz.dto.response.QuizSolveResponse;
import com.shinhan.dongibuyeo.domain.quiz.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/quiz")
public class QuizController {
    private QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<QuizResponse> getRandomQuiz(@PathVariable("memberId") UUID memberId) {
        return ResponseEntity.ok(quizService.getRandomQuiz(memberId));
    }

    @PostMapping("/solve")
    public ResponseEntity<QuizSolveResponse> solveQuiz(@RequestBody QuizSolveRequest request) {
        return ResponseEntity.ok(quizService.solveQuiz(request));
    }

    @GetMapping("date/{memberId}")
    public ResponseEntity<List<QuizSolveResponse>> getMemberMonthSolvedList(@PathVariable UUID memberId,@RequestParam Integer year, @RequestParam Integer month) {
        return ResponseEntity.ok(quizService.getMemberDateSolvedList(memberId,year,month));
    }

    @GetMapping("already/{memberId}")
    public ResponseEntity<Boolean> getMemberAlreadySolved(@PathVariable UUID memberId) {
        return ResponseEntity.ok(quizService.alreadyToday(memberId));
    }
}
