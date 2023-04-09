package com.jpshoppingmall.domain.product.service;

import com.jpshoppingmall.domain.product.entity.PurchaseCountHistory;
import com.jpshoppingmall.domain.product.repository.PurchaseCountHistoryRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PurchaseCountHistoryWriteService {

    private final PurchaseCountHistoryRepository purchaseCountHistoryRepository;

    @Transactional
    public void saveNewHistory(Long productId, Integer count) {
        purchaseCountHistoryRepository.save(PurchaseCountHistory.builder()
            .productId(productId)
            .quantity(count)
            .build());
    }

    @Transactional
    public void saveNewHistory(LocalDateTime standardTime) {
        purchaseCountHistoryRepository.deleteAllByBeforeStandardTime(standardTime);
    }
}
