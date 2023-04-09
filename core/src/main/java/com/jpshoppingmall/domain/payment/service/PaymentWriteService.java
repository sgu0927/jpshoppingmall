package com.jpshoppingmall.domain.payment.service;

import com.jpshoppingmall.domain.payment.dto.PaymentSuccessDto;
import com.jpshoppingmall.domain.payment.entity.Payment;
import com.jpshoppingmall.domain.payment.repository.PaymentRepository;
import com.jpshoppingmall.type.EnumMaster.PaymentType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PaymentWriteService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public Payment addPayment(PaymentSuccessDto paymentSuccess) {
        return paymentRepository.save(Payment.builder()
            .paymentType(PaymentType.CARD)
            .price(paymentSuccess.totalPrice())
            .discountPrice(paymentSuccess.totalDiscountPrice())
            .paidAmount(paymentSuccess.totalPrice() - paymentSuccess.totalDiscountPrice())
            .build());
    }
}
