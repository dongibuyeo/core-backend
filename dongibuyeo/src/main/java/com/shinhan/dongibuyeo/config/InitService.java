package com.shinhan.dongibuyeo.config;

import com.shinhan.dongibuyeo.domain.member.service.MemberService;
import com.shinhan.dongibuyeo.domain.product.client.ProductClient;
import com.shinhan.dongibuyeo.domain.product.dto.client.ShinhanProductRequest;
import com.shinhan.dongibuyeo.domain.product.dto.client.ShinhanProductResponse;
import com.shinhan.dongibuyeo.domain.product.entity.Product;
import com.shinhan.dongibuyeo.domain.product.mapper.ProductMapper;
import com.shinhan.dongibuyeo.domain.product.repository.ProductRepository;
import com.shinhan.dongibuyeo.global.header.GlobalAdminHeader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class InitService implements ApplicationListener<ContextRefreshedEvent> {

    private final MemberService memberService;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductClient productClient;

    @Value("${shinhan.key}")
    private String apiKey;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        createAdminAccountAndProduct();
    }

    @Transactional
    public void createAdminAccountAndProduct() {
        createAdminAccount();
        createAdminProduct();
    }

    private void createAdminAccount() {
        memberService.findAdminMember();
    }

    public void createAdminProduct() {
        String bankCode = "001";
        String accountName = "ADMIN_PRODUCT";
        String accountDescription = "관리자 전용 상품.";

        ShinhanProductRequest tmpRequest = new ShinhanProductRequest(
                new GlobalAdminHeader("createDemandDeposit", apiKey),
                bankCode,
                accountName,
                accountDescription
        );

        ShinhanProductResponse shinhanResponse = productClient.makeProduct(tmpRequest);

        Product product = productRepository.save(productMapper.toEntity(shinhanResponse));

    }
}