package com.shinhan.dongibuyeo.domain.challenge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ScoreEntry {
    private String description;
    private int points;
    private int totalPointsAfter;
}
