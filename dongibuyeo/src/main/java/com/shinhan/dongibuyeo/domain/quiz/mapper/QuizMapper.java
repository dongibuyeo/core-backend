package com.shinhan.dongibuyeo.domain.quiz.mapper;

import com.shinhan.dongibuyeo.domain.quiz.dto.request.QuizMakeRequest;
import com.shinhan.dongibuyeo.domain.quiz.dto.response.QuizResponse;
import com.shinhan.dongibuyeo.domain.quiz.dto.response.QuizSolveResponse;
import com.shinhan.dongibuyeo.domain.quiz.entity.Quiz;
import com.shinhan.dongibuyeo.domain.quiz.entity.QuizMember;
import org.springframework.stereotype.Component;

@Component
public class QuizMapper {

    public Quiz toQuizEntity(QuizMakeRequest request) {
        return new Quiz(
                request.getQuestion(),
                request.getAnswer()
        );
    }

    public QuizResponse toQuizResponse(Quiz quiz) {
        return new QuizResponse(
                quiz.getId(),
                quiz.getQuestion(),
                quiz.getAnswer()
        );
    }

    public QuizSolveResponse toQuizSolveResponse(QuizMember quiz) {
        return new QuizSolveResponse(
                quiz.getId(),
                quiz.getSolvedAt()
        );
    }
}
