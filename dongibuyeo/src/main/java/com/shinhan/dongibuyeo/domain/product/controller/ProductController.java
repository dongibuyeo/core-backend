package com.shinhan.dongibuyeo.domain.product.controller;

import com.shinhan.dongibuyeo.domain.product.dto.request.MakeProductRequest;
import com.shinhan.dongibuyeo.domain.product.dto.response.MakeProductResponse;
import com.shinhan.dongibuyeo.domain.product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<MakeProductResponse> makeProduct(@RequestBody MakeProductRequest request) {
        return ResponseEntity.ok(productService.makeProduct(request));
    }


}
