package com.jpshoppingmall.domain.product.repository;

import com.jpshoppingmall.domain.product.dto.ProductDetailDto;
import com.jpshoppingmall.domain.product.entity.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductCustomRepository {

    List<Product> getTopNineProductsWithImages();
    Product getProductWithImages(Long productId);

    Page<ProductDetailDto> getCategoryProducts(List<Long> categoryIds, Pageable pageable);
}
