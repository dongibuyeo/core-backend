package com.shinhan.dongibuyeo.domain.quiz.repository;

import com.shinhan.dongibuyeo.domain.member.entity.Member;
import com.shinhan.dongibuyeo.domain.quiz.entity.QuizMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface QuizMemberRepository extends JpaRepository<QuizMember, Long> {

    @Query("SELECT qm FROM QuizMember qm WHERE qm.member.id = :memberId AND year(qm.solvedAt) = :year AND month(qm.solvedAt) = :month")
    List<QuizMember> findAllByMemberAndDate(@Param("year") int year, @Param("month") int month, @Param("memberId") UUID memberId);

    @Query("SELECT COUNT(qm) > 0 FROM QuizMember qm WHERE qm.member.id = :memberId AND year(qm.solvedAt) = :year AND month(qm.solvedAt) = :month AND day(qm.solvedAt) = :day")
    Boolean existsByMemberAndDate(@Param("memberId") UUID memberId, @Param("year") int year, @Param("month") int month, @Param("day") int day);
}
