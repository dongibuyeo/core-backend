package com.shinhan.dongibuyeo.domain.challenge.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScoreEntry {
    private String description;
    private int score;
    private int currentTotalScore;
}