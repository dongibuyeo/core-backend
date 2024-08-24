package com.shinhan.dongibuyeo.domain.product.repository;

import com.shinhan.dongibuyeo.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findByAccountName(String accountName);
}
