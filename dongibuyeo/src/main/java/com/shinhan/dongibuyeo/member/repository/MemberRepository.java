package com.shinhan.dongibuyeo.member.repository;

import com.shinhan.dongibuyeo.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
}
