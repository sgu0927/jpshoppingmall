package com.jpshoppingmall.service;

import com.jpshoppingmall.domain.product.service.PurchaseCountHistoryReadService;
import com.jpshoppingmall.domain.product.service.PurchaseCountReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PurchaseCountService {

    private final PurchaseCountHistoryReadService purchaseCountHistoryReadService;
    private final PurchaseCountReadService purchaseCountReadService;

    @Cacheable(keyGenerator = "purchaseCountCacheKeyGenerator", cacheNames = "purchaseCountCache")
    public Integer getCurrentPurchaseCount(Long productId) {
        return getPurchaseCountFromRdb(productId);
    }

    private int getPurchaseCountFromRdb(Long productId) {
        Integer notProcessedPurchaseCount = purchaseCountHistoryReadService.getNotProcessedPurchaseCount(
            productId);

        Integer totalPurchaseCount = purchaseCountReadService.getPurchaseCount(productId);
        return notProcessedPurchaseCount + totalPurchaseCount;
    }
}
