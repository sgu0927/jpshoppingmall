package com.jpshoppingmall.domain.cart.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record CartProductDto(
    @NotNull
    Long cartId,
    @NotNull
    Long productId,
    @NotBlank
    String name,
    @NotNull
    Integer price,
    Integer discountPercent,

    @NotNull
    Integer itemCount,
    @NotBlank
    String productTitleImagePath
) {

}
