package com.jpshoppingmall.order.service;

import com.jpshoppingmall.domain.product.dto.ProductCountDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductCountDtoProducerService {

    private static final String TOPIC = "jpshoppingmall.product.count";
    private final KafkaTemplate<String, ProductCountDto> kafkaTemplateProductCountDto;

    public void sendMessage(ProductCountDto productCountDto) {
        log.info(
            "[ProductCountDtoProducerService] try produce productCountDto - productCountDto :: {}",
            productCountDto.toString());

        ListenableFuture<SendResult<String, ProductCountDto>> future = kafkaTemplateProductCountDto.send(
            TOPIC, productCountDto);

        future.addCallback(new ListenableFutureCallback<SendResult<String, ProductCountDto>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error(
                    "[ProductCountDtoProducerService] Unable to send message=[" + productCountDto
                        + "] due to : " + ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, ProductCountDto> result) {
                log.info(
                    "[ProductCountDtoProducerService] Sent message=[" + productCountDto
                        + "] with offset=[" + result.getRecordMetadata()
                        .offset() + "]");
            }
        });
    }
}
