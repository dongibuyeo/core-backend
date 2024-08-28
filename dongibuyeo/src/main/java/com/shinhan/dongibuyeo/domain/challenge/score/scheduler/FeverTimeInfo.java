package com.shinhan.dongibuyeo.domain.challenge.score.scheduler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeverTimeInfo {
    LocalDateTime start;
    LocalDateTime end;
    String description;
    int score;
}
