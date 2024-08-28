package com.shinhan.dongibuyeo.domain.member.repository;

import com.shinhan.dongibuyeo.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {

    @Query("SELECT m " +
            "FROM Member m " +
            "WHERE m.id = :memberId ")
    Optional<Member> findMemberById(UUID memberId);
    Optional<Member> findMemberByEmail(String email);

}
