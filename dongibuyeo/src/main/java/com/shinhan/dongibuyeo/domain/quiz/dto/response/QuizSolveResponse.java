package com.shinhan.dongibuyeo.domain.quiz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuizSolveResponse {
    private UUID quizMemberId;
    private LocalDateTime solvedAt;
}
