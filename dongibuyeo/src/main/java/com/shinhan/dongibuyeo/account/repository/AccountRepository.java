package com.shinhan.dongibuyeo.account.repository;

import com.shinhan.dongibuyeo.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
}
