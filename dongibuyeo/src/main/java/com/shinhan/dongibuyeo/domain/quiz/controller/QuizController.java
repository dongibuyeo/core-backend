package com.shinhan.dongibuyeo.domain.quiz.controller;

import com.shinhan.dongibuyeo.domain.quiz.dto.response.QuizResponse;
import com.shinhan.dongibuyeo.domain.quiz.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    }
}
