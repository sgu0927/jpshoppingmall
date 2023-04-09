package com.jpshoppingmall.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class EnumMaster {
    public enum Role {
        USER, SELLER, ADMIN;
    }

    public enum GenderType {
        M,W
    }

    // 회원 등급
    public enum Grade {
        BASIC, VIP
    }

    public enum SortType {
        NEWEST, PRICE_ASC, PRICE_DESC, RATING_DESC, ORDERED_DESC, DISCOUNT_PERCENT_DESC
    }

    public enum PagingType {
        PRODUCT, CART, ORDER, ORDER_DETAIL, REVIEW
    }

    public enum PaymentType {
        CARD
    }

    @Getter
    @RequiredArgsConstructor
    public enum OrderStatus {
        PAYMENT_WAITING("결제전"), PREPARING("상품 준비중"), SHIPPED("출고완료"),
        DELIVERING("배송중"), DELIVERY_COMPLETED("배송완료"), CHECKING_REFUND("환불신청됨"),
        REFUND_BEING_PROCESSED("환불 진행중"), REFUNDED("환불완료"), CANCELED("주문취소");
        private final String description;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ProductStatus {
        SOLDOUT("품절"), TEMPSOLDOUT("일시품절"), SALE("판매중");
        private final String description;
    }
}
