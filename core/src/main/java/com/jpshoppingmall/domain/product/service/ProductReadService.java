package com.jpshoppingmall.domain.product.service;

import com.jpshoppingmall.domain.product.dto.ProductDetailDto;
import com.jpshoppingmall.domain.product.entity.Product;
import com.jpshoppingmall.domain.product.repository.ProductRepository;
import com.jpshoppingmall.exception.CommonException;
import com.jpshoppingmall.exception.CommonExceptionType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductReadService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Product getProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new CommonException(
            CommonExceptionType.CANNOT_FOUND_PRODUCT));
    }

    @Transactional(readOnly = true)
    public List<Product> getTopNineProductWithImages() {
        return productRepository.getTopNineProductsWithImages();
    }

    @Transactional(readOnly = true)
    public Product getProductWithImages(Long productId) {
        return productRepository.getProductWithImages(productId);
    }

    @Transactional(readOnly = true)
    public Page<ProductDetailDto> getCategoryProducts(List<Long> categoryIds, Pageable pageable) {
        return productRepository.getCategoryProducts(categoryIds, pageable);
    }
}
