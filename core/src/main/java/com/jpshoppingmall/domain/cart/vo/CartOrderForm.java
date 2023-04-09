package com.jpshoppingmall.domain.cart.vo;

import java.util.Set;
import lombok.Data;

@Data
public class CartOrderForm {
    private Set<Long> cartIds;
    private String postCode;
    private String roadNameAddress;
    private String detailAddress;
    private String receiverPhone;
    private String deliveryMessage;
}
