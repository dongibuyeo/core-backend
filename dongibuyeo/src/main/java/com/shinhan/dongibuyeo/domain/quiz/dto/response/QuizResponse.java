package com.shinhan.dongibuyeo.domain.quiz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuizResponse {
    private UUID id;
    private String question;
    private Boolean answer;
}
