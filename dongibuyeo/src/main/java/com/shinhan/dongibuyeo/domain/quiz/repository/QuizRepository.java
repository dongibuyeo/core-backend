package com.shinhan.dongibuyeo.domain.quiz.repository;

import com.shinhan.dongibuyeo.domain.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QuizRepository extends JpaRepository<Quiz, UUID> {

}
