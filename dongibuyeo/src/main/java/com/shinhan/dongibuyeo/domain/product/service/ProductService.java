package com.shinhan.dongibuyeo.domain.product.service;

import com.shinhan.dongibuyeo.domain.product.client.ProductClient;
import com.shinhan.dongibuyeo.domain.product.dto.client.ShinhanProductRequest;
import com.shinhan.dongibuyeo.domain.product.dto.client.ShinhanProductResponse;
import com.shinhan.dongibuyeo.domain.product.dto.request.MakeProductRequest;
import com.shinhan.dongibuyeo.domain.product.dto.response.MakeProductResponse;
import com.shinhan.dongibuyeo.domain.product.entity.Product;
import com.shinhan.dongibuyeo.domain.product.mapper.ProductMapper;
import com.shinhan.dongibuyeo.domain.product.repository.ProductRepository;
import com.shinhan.dongibuyeo.global.header.GlobalAdminHeader;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Value("${shinhan.key}")
    private String apiKey;
    private final ProductRepository productRepository;
    private final ProductClient productClient;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductClient productClient, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productClient = productClient;
        this.productMapper = productMapper;
    }

    @Transactional
    public MakeProductResponse makeProduct(MakeProductRequest request) {

        ShinhanProductRequest tmpRequest =  new ShinhanProductRequest(
                new GlobalAdminHeader("createDemandDeposit", apiKey),
                request.getBankCode(),
                request.getAccountName(),
                request.getAccountDescription()
        );

        ShinhanProductResponse shinhanResponse = productClient.makeProduct(
                tmpRequest
        );

        Product product = productRepository.save(productMapper.toEntity(shinhanResponse));

        return productMapper.toResponse(product);
    }
}
