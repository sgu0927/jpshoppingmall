package com.jpshoppingmall.domain.orderitem.dto;

import com.jpshoppingmall.type.EnumMaster.OrderStatus;
import java.time.LocalDateTime;

public record ProductOrderDetailDto(
    Long id,
    Long productId,
    String productName,
    Integer itemCount,
    Integer orderPrice,
    String productTitleImagePath,
    OrderStatus status,
    Boolean hasReview,
    LocalDateTime deliveredDateTime
) {

}
