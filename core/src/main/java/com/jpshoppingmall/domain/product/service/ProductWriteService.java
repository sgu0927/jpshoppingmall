package com.jpshoppingmall.domain.product.service;

import com.jpshoppingmall.domain.category.entity.Category;
import com.jpshoppingmall.domain.product.dto.RegisterProductRequestDto;
import com.jpshoppingmall.domain.product.dto.RegisterProductResponseDto;
import com.jpshoppingmall.domain.product.entity.Product;
import com.jpshoppingmall.domain.product.repository.ProductRepository;
import com.jpshoppingmall.domain.product.vo.ProductRegisterForm;
import com.jpshoppingmall.type.EnumMaster.ProductStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductWriteService {

    private final ProductRepository productRepository;

    @Transactional
    public RegisterProductResponseDto addProduct(Category category,
        RegisterProductRequestDto productRequestDto) {
        var product = Product.builder()
            .category(category)
            .name(productRequestDto.name())
            .price(productRequestDto.price())
            .discountPercent(productRequestDto.discountPercent() == null ? 0
                : productRequestDto.discountPercent())
            .totalCount(productRequestDto.totalCount())
            .productStatus(ProductStatus.SALE)
            .rating(0F)
            .reviewCount(0)
            .isConfirm(false)
            .build();

        final var savedProduct = productRepository.save(product);

        return savedProduct.toResponseDto();
    }

    @Transactional
    public Product addProduct(Category category,
        ProductRegisterForm registerForm) {
        var product = Product.builder()
            .category(category)
            .name(registerForm.getName())
            .price(registerForm.getPrice())
            .discountPercent(registerForm.getDiscountPercent() == null ? 0
                : registerForm.getDiscountPercent())
            .totalCount(registerForm.getTotalCount())
            .productStatus(ProductStatus.SALE)
            .rating(0F)
            .reviewCount(0)
            .isConfirm(false)
            .build();

        final var savedProduct = productRepository.save(product);

        return savedProduct;
    }

    @Transactional
    public void delete(Long productId) {
        productRepository.deleteById(productId);
    }
}
