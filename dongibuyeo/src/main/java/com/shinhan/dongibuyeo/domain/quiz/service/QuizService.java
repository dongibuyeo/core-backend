package com.shinhan.dongibuyeo.domain.quiz.service;

import com.shinhan.dongibuyeo.domain.quiz.dto.response.QuizResponse;
import com.shinhan.dongibuyeo.domain.quiz.repository.QuizRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class QuizService {
    private QuizRepository quizRepository;

    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public QuizResponse getRandomQuiz(UUID memberId) {

    }
}
