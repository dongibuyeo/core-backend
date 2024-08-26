package com.shinhan.dongibuyeo.domain.challenge.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScoreDetail {
    private String description;
    private int score;
    private int currentTotalScore;
}