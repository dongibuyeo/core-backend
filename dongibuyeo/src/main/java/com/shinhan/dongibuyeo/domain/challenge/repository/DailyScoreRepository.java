package com.shinhan.dongibuyeo.domain.challenge.repository;

import com.shinhan.dongibuyeo.domain.challenge.entity.DailyScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DailyScoreRepository extends JpaRepository<DailyScore, UUID> {
}
