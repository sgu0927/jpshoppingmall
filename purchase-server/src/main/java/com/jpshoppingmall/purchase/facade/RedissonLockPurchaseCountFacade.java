package com.jpshoppingmall.purchase.facade;

import com.jpshoppingmall.domain.product.service.ProductReadService;
import com.jpshoppingmall.domain.product.service.PurchaseCountHistoryWriteService;
import com.jpshoppingmall.purchase.service.PurchaseCountService;
import com.jpshoppingmall.util.RedisUtil;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedissonLockPurchaseCountFacade {

    private final RedissonClient redissonClient;
    private final StringRedisTemplate redisTemplate;
    private final ProductReadService productReadService;
    private final PurchaseCountService purchaseCountService;
    private final PurchaseCountHistoryWriteService purchaseCountHistoryWriteService;

    public void increasePurchaseCount(long productId, int count) {
        String key = RedisUtil.createPurchaseCountKey(productId);
        String purchaseCountLockKey = RedisUtil.createPurchaseCountLockKey(productId);

        RLock lock = redissonClient.getLock(purchaseCountLockKey);
        long totalPurchaseCount;
        long productTotalCount;

        try {
            // 획득시도 시간, 락 점유 시간
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);

            if (!available) {
                log.info("[RedissonLockPurchaseCountFacade] lock 획득 실패 - redisson getLock timeout");
                return;
            }

            totalPurchaseCount = purchaseCountService.getCurrentPurchaseCount(productId);
            productTotalCount = getProductTotalCount(productId);

            if (productTotalCount >= totalPurchaseCount + count) {
                purchaseCountHistoryWriteService.saveNewHistory(productId, count);
                redisTemplate.opsForValue().increment(key, count);
                log.info("[RedissonLockPurchaseCountFacade] increment success with Redis!");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    private Integer getProductTotalCount(long productId) {
        return productReadService.getProduct(productId).getTotalCount();
    }
}
