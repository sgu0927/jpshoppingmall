package com.jpshoppingmall.domain.product.repository;

public interface PurchaseCountHistoryCustomRepository {

    Integer getNotProcessedPurchaseCount(Long productId);
}
