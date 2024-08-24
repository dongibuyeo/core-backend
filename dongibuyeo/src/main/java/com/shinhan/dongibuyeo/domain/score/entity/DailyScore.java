package com.shinhan.dongibuyeo.domain.score.entity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.f4b6a3.ulid.UlidCreator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyScore {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id = UlidCreator.getMonotonicUlid().toUuid();

    private LocalDate date;

    @Column(columnDefinition = "JSON")
    private String scoreDetails;  // JSON 형식으로 점수 세부 정보 저장

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Builder
    public DailyScore(LocalDate date, String scoreDetails) {
        this.date = date;
        this.scoreDetails = scoreDetails;
    }

    public int getTotalScore() {
        try {
            Map<String, Integer> scores = objectMapper.readValue(scoreDetails, new TypeReference<>() {});
            return scores.values().stream().mapToInt(Integer::intValue).sum();
        } catch (Exception e) {
            // TODO 세부 예외 처리
            throw new RuntimeException("Error calculating total score", e);
        }
    }

}