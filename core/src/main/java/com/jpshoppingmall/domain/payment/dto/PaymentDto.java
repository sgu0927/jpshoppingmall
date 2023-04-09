package com.jpshoppingmall.domain.payment.dto;

import java.time.LocalDateTime;

public record PaymentDto(
    Integer price,
    Integer discountPrice,
    Integer paidAmount,
    LocalDateTime createdDateTime
) {

}
