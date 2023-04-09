package com.jpshoppingmall.domain.cart.dto;

public record CartInfoDto(
    Long cartId,
    String productName,
    Integer price,
    Integer discountPrice
) {

}
