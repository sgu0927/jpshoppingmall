package com.jpshoppingmall.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonExceptionType {
    INTERNAL_SERVER_ERROR("COMMON-001", "Internal server error"),
    INVALID_PARAMS("COMMON-002", "Invalid params"),
    UNSUPPORTED_SORT_TYPE("COMMON-003", "unsupported sort type"),
    INVALID_HEADER_PARAMS("COMMON-004", "Invalid header params"),
    SSE_SEND_ERROR("COMMON-005", "Sse Send Error"),

    // member
    CANNOT_FOUND_MEMBER("MEMBER-001", "멤버를 찾을 수 없습니다."),
    DUPLICATE_MEMBER_EMAIL("MEMBER-002", "멤버 계정의 이메일이 중복되었습니다."),
    DUPLICATE_MEMBER_NICKNAME("MEMBER-003", "멤버 계정의 닉네임이 중복되었습니다."),
    MEMBER_IS_DELETED("MEMBER-004", "멤버 계정이 삭제되었습니다."),
    EXPIRED_EMAIL_VERIFICATION_CODE("MEMBER-005", "멤버 계정의 이메일 인증 코드가 만료되었습니다."),
    INVALID_EMAIL_VERIFICATION_CODE("MEMBER-006", "멤버 계정의 이메일 인증 코드와 일치하지 않습니다."),
    TOO_SHORT_PASSWORD("MEMBER-007", "비밀번호는 최소 6자 이상이어야 합니다."),
    TOO_LONG_PASSWORD("MEMBER-007", "비밀번호는 최소 6자 이상, 최대 20자입니다."),

    // product
    CANNOT_FOUND_PRODUCT("PRODUCT-001", "상품을 찾을 수 없습니다."),
    CANNOT_FOUND_PRODUCT_IMAGE("PRODUCT-002", "상품 사진을 찾을 수 없습니다."),
    CANNOT_FOUND_PRODUCT_RATING("PRODUCT-003", "상품 리뷰 점수를 찾을 수 없습니다."),

    // order
    CANNOT_FOUND_ORDER("ORDER-001", "주문내역을 찾을 수 없습니다."),

    // order item
    CANNOT_FOUND_ORDER_ITEM("ORDER-ITEM-001", "주문 상품을 찾을 수 없습니다."),
    
    // category
    CANNOT_FOUND_CATEGORY("CATEGORY-001", "카테고리를 찾을 수 없습니다."),

    // cart
    CANNOT_FOUND_CART("CART-001", "장바구니를 찾을 수 없습니다."),

    // review
    CANNOT_FOUND_REVIEW("REVIEW-001", "리뷰를 찾을 수 없습니다."),
    ALREADY_HAS_REVIEW("REVIEW-002", "이미 작성된 주문 상품의 리뷰입니다."),
    // payment
    CANNOT_FOUND_PAYMENT("PAYMENT-001", "결제내역을 찾을 수 없습니다."),

    // token
    CANNOT_FOUND_TOKEN("JWT-001", "토큰을 찾을 수 없습니다."),
    ACCESS_DENIED_ERROR("JWT-002", "Do not have permission"),
    ACCESS_TOKEN_EXPIRATION("JWT-003", "Access token expiration"),
    REFRESH_TOKEN_EXPIRATION("JWT-004", "Refresh token expiration"),
    INVALID_TOKEN("JWT-005", "Invalid token"),
    TOKENS_HAVE_DIFFERENT_OWNERS("JWT-006", "Tokens have different owners");

    // private final int status;
    private final String code;
    private final String message;
}
