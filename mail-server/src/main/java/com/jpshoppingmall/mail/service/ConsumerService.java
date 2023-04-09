package com.jpshoppingmall.mail.service;

import com.jpshoppingmall.domain.payment.dto.PaymentSuccessDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ConsumerService {

    private final MailService mailService;

    @KafkaListener(topics = "jpshoppingmall.email.verification", containerFactory = "kafkaStringListener")
    public void receiveEmailVerification(String email) {
        mailService.sendVerificationMail(email);
    }

    @KafkaListener(topics = "jpshoppingmall.payment.success", containerFactory = "kafkaPaymentSuccessDtoListener")
    public void receivePayment(PaymentSuccessDto paymentSuccessDto) {
        mailService.sendPaymentSuccessMail(paymentSuccessDto);
    }
}
