package com.jpshoppingmall.domain.order.dto;

import javax.validation.constraints.NotBlank;

public record CreateProductOrderDto(
    @NotBlank
    String roadNameAddress,
    String detailAddress,
    @NotBlank
    String receiverPhone
    // TODO :: OrderItem 각각의 결제 정보..
) {

}
