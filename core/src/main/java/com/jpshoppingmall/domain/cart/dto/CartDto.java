package com.jpshoppingmall.domain.cart.dto;

import javax.validation.constraints.NotNull;

public record CartDto(
    @NotNull
    Integer itemCount,
    Long productId
) {

}
