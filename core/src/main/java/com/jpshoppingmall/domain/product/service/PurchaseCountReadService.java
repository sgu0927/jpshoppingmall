package com.jpshoppingmall.domain.product.service;

import com.jpshoppingmall.domain.product.entity.PurchaseCount;
import com.jpshoppingmall.domain.product.repository.PurchaseCountRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PurchaseCountReadService {

    private final PurchaseCountRepository purchaseCountRepository;

    @Transactional(readOnly = true)
    public Integer getPurchaseCount(Long productId) {
        Optional<PurchaseCount> purchaseCountOptional = purchaseCountRepository.findByProductId(productId);
        if(purchaseCountOptional.isPresent()) {
            return purchaseCountOptional.get().getQuantity();
        }

        return 0;
    }
}
