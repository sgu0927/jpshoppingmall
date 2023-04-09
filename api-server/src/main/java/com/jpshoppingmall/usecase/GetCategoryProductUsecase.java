package com.jpshoppingmall.usecase;

import com.jpshoppingmall.domain.category.service.CategoryReadService;
import com.jpshoppingmall.domain.product.dto.ProductDetailDto;
import com.jpshoppingmall.domain.product.service.ProductReadService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetCategoryProductUsecase {

    private final CategoryReadService categoryReadService;
    private final ProductReadService productReadService;

    @Transactional(readOnly = true)
    public Page<ProductDetailDto> execute(Long categoryId, Pageable pageable) {
        List<Long> categoryIds = categoryReadService.getChildCategoryIds(categoryId);
        return productReadService.getCategoryProducts(categoryIds, pageable);
    }
}
