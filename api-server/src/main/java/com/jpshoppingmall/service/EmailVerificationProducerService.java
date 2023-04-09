package com.jpshoppingmall.service;

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
public class EmailVerificationProducerService {

    private static final String TOPIC = "jpshoppingmall.email.verification";
    private final KafkaTemplate<String, String> kafkaTemplateString;

    public void sendMessage(String email) {
        log.info(
            "[EmailVerificationService] try produce email to verify email in Join - email :: {}"
                + email);

        ListenableFuture<SendResult<String, String>> future = kafkaTemplateString.send(TOPIC, email);

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("[EmailVerificationService] Unable to send message=[" + email + "] due to : " + ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info(
                    "[EmailVerificationService] Sent message=[" + email + "] with offset=[" + result.getRecordMetadata()
                        .offset() + "]");
            }
        });
    }
}
