package com.shinhan.dongibuyeo.domain.consume.repository;

import com.shinhan.dongibuyeo.domain.consume.entity.Consumption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConsumptionRepository extends JpaRepository<Consumption, UUID> {

}
