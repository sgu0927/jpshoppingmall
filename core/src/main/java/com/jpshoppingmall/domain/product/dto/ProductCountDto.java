package com.jpshoppingmall.domain.product.dto;

public record ProductCountDto(
    Long orderId,
    Long productId,
    Integer count
) {

}
