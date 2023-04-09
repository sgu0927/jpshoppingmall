package com.jpshoppingmall.domain.product.service;

import com.jpshoppingmall.domain.product.entity.PurchaseCount;
import com.jpshoppingmall.domain.product.repository.PurchaseCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PurchaseCountWriteService {

    private final PurchaseCountRepository purchaseCountRepository;

    @Transactional
    public void saveNewProductCount(Long productId) {
        purchaseCountRepository.save(PurchaseCount.builder()
            .productId(productId)
            .quantity(0)
            .build());
    }
}
