package com.shinhan.dongibuyeo.domain.product.mapper;

import com.shinhan.dongibuyeo.domain.product.dto.client.ShinhanProductResponse;
import com.shinhan.dongibuyeo.domain.product.dto.response.MakeProductResponse;
import com.shinhan.dongibuyeo.domain.product.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ShinhanProductResponse request) {
        MakeProductResponse tmpResponse = request.getRec();

        return new Product(
                tmpResponse.getAccountTypeUniqueNo(),
                tmpResponse.getBankCode(),
                tmpResponse.getBankName(),
                tmpResponse.getAccountTypeCode(),
                tmpResponse.getAccountTypeName(),
                tmpResponse.getAccountName(),
                tmpResponse.getAccountDescription(),
                tmpResponse.getAccountType()
        );
    }

    public MakeProductResponse toResponse(Product product) {
        return new MakeProductResponse(
                product.getAccountTypeUniqueNo(),
                product.getBankCode(),
                product.getBankName(),
                product.getAccountTypeCode(),
                product.getAccountTypeName(),
                product.getAccountName(),
                product.getAccountDescription(),
                product.getAccountType()
        );
    }
}
