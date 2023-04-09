package com.jpshoppingmall.domain.order.dto;

import com.jpshoppingmall.common.Address;
import java.time.LocalDateTime;

public record ProductOrderDto(
    Long id,
    String displayName,
    Address roadNameAddress,
    String receiverPhone,
    String deliveryMessage,
    Integer paidAmount,
    LocalDateTime orderedDateTime
) {

}
