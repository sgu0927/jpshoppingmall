package com.jpshoppingmall.domain.product.service;

import com.jpshoppingmall.domain.product.repository.PurchaseCountHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PurchaseCountHistoryReadService {

    private final PurchaseCountHistoryRepository purchaseCountHistoryRepository;

    @Transactional
    public Integer getNotProcessedPurchaseCount(Long productId) {
        return purchaseCountHistoryRepository.getNotProcessedPurchaseCount(productId);
    }
}
