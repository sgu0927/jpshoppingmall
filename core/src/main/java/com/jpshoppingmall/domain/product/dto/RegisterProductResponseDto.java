package com.jpshoppingmall.domain.product.dto;

public record RegisterProductResponseDto(
    String name,
    Integer price,
    Integer discountPercent,
    Integer totalCount
) {
}
