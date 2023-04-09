package com.jpshoppingmall.usecase;

import com.jpshoppingmall.domain.product.dto.ProductDetailDto;
import com.jpshoppingmall.domain.product.entity.Product;
import com.jpshoppingmall.domain.product.service.ProductReadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetProductDetailUsecase {

    private final ProductReadService productReadService;

    @Transactional(readOnly = true)
    public ProductDetailDto execute(Long productId) {
        final Product product = productReadService.getProductWithImages(productId);
        return product.toDetailDto();
    }
}
