package com.shinhan.dongibuyeo.domain.quiz.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuizSolveRequest {
    private UUID quizId;
    private UUID memberId;
}
