package com.jpshoppingmall.purchase.service;

import com.jpshoppingmall.domain.product.dto.ProductCountDto;
import com.jpshoppingmall.purchase.facade.RedissonLockPurchaseCountFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConsumerService {

    private final RedissonLockPurchaseCountFacade redissonLockPurchaseCountFacade;

    @KafkaListener(topics = "jpshoppingmall.product.count", containerFactory = "kafkaProductCountDtoListener")
    public void receiveProductCountDto(ProductCountDto productCountDto) {
        log.info("[purchase-server] ProductCountDto 수신 완료. productCountDto :: {}",
            productCountDto.toString());

        try {
            redissonLockPurchaseCountFacade.increasePurchaseCount(productCountDto.productId(),
                productCountDto.count());
            log.info("[purchase-server] ProductCountDto 처리 완료. productCountDto :: {}",
                productCountDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("[purchase-server] ProductCountDto 처리 실패. productCountDto :: {}",
                productCountDto);
        }
    }
}
