package com.jpshoppingmall.domain.payment.service;

import com.jpshoppingmall.domain.payment.dto.PaymentDto;
import com.jpshoppingmall.domain.payment.entity.Payment;
import com.jpshoppingmall.domain.payment.repository.PaymentRepository;
import com.jpshoppingmall.exception.CommonException;
import com.jpshoppingmall.exception.CommonExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PaymentReadService {

    private final PaymentRepository paymentRepository;

    @Transactional(readOnly = true)
    public Payment findById(Long paymentId) {
        return paymentRepository.findById(paymentId)
            .orElseThrow(() -> new CommonException(
                CommonExceptionType.CANNOT_FOUND_PAYMENT));
    }

    @Transactional(readOnly = true)
    public PaymentDto findDtoById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new CommonException(
                CommonExceptionType.CANNOT_FOUND_PAYMENT));

        return new PaymentDto(payment.getPrice(), payment.getDiscountPrice(),
            payment.getPaidAmount(), payment.getCreatedDateTime());
    }
}
