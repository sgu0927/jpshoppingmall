package com.jpshoppingmall.domain.payment.dto;

import java.util.Map;

public record PaymentSuccessDto(
    Long paymentId,
    Long memberId,
    String memberEmail,
    String memberName,
    Map<Long, Integer> amountByCartId,
    String displayName,
    Integer totalPrice,
    Integer totalDiscountPrice,
    String postCode,
    String roadNameAddress,
    String detailAddress,
    String receiverPhone,
    String deliveryMessage
) {

}
