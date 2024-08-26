package com.shinhan.dongibuyeo.domain.account.repository;

import com.shinhan.dongibuyeo.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    Optional<Account> findByAccountNo(String accountNo);
}
