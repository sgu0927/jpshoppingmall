package com.jpshoppingmall.service;

import com.jpshoppingmall.domain.payment.dto.PaymentSuccessDto;
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
public class PaymentProducerService {

    private static final String TOPIC = "jpshoppingmall.payment.success";
    private final KafkaTemplate<String, PaymentSuccessDto> kafkaTemplatePaymentSuccessDto;

    public void sendMessage(PaymentSuccessDto paymentSuccessDto) {
        log.info(
            "[PaymentProducerService] try produce email to verify email in Join - paymentSuccessDto :: {}"
                + paymentSuccessDto.toString());

        ListenableFuture<SendResult<String, PaymentSuccessDto>> future = kafkaTemplatePaymentSuccessDto.send(
            TOPIC, paymentSuccessDto);

        future.addCallback(new ListenableFutureCallback<SendResult<String, PaymentSuccessDto>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("[PaymentProducerService] Unable to send message=[" + paymentSuccessDto
                    + "] due to : " + ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, PaymentSuccessDto> result) {
                log.info(
                    "[PaymentProducerService] Sent message=[" + paymentSuccessDto
                        + "] with offset=[" + result.getRecordMetadata()
                        .offset() + "]");
            }
        });
    }
}
