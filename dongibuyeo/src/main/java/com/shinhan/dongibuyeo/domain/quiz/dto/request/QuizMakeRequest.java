package com.shinhan.dongibuyeo.domain.quiz.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuizMakeRequest {
    private String question;
    private Boolean answer;
}
