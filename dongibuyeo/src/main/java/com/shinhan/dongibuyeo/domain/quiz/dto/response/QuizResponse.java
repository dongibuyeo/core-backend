package com.shinhan.dongibuyeo.domain.quiz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuizResponse {
    private String question;
    private Boolean answer;
}
