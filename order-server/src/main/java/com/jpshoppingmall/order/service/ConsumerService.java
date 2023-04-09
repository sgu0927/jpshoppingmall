package com.jpshoppingmall.order.service;

import com.jpshoppingmall.domain.payment.dto.PaymentSuccessDto;
import com.jpshoppingmall.domain.product.dto.ProductCountDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConsumerService {

    private final OrderService orderService;
    private final ProductCountDtoProducerService productCountDtoProducerService;

    @KafkaListener(topics = "jpshoppingmall.payment.success", containerFactory = "kafkaPaymentSuccessDtoListener")
    public void receivePayment(PaymentSuccessDto paymentSuccessDto) {
        log.info("[order-server] PaymentSuccessDto 수신 완료. paymentSuccessDto :: {}", paymentSuccessDto.toString());

        List<ProductCountDto> productCountDtoList = orderService.createOrder(paymentSuccessDto);
        for (ProductCountDto productCountDto : productCountDtoList) {
            productCountDtoProducerService.sendMessage(productCountDto);
        }

        log.info("[order-server] ProductCountDto 전송 완료. productCountDtoList :: {}",
            productCountDtoList.toString());
    }
}
