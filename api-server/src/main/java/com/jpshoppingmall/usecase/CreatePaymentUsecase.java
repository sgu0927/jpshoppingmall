package com.jpshoppingmall.usecase;

import com.jpshoppingmall.domain.payment.dto.PaymentSuccessDto;
import com.jpshoppingmall.domain.payment.entity.Payment;
import com.jpshoppingmall.domain.payment.service.PaymentWriteService;
import com.jpshoppingmall.service.PaymentProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CreatePaymentUsecase {

    private final PaymentProducerService paymentProducerService;
    private final PaymentWriteService paymentWriteService;

    @Transactional
    public void execute(PaymentSuccessDto paymentSuccess) {
        Payment payment = paymentWriteService.addPayment(paymentSuccess);
        paymentProducerService.sendMessage(
            new PaymentSuccessDto(payment.getId(), paymentSuccess.memberId(),
                paymentSuccess.memberEmail(), paymentSuccess.memberName(),
                paymentSuccess.amountByCartId(), paymentSuccess.displayName(),
                payment.getPrice(), payment.getDiscountPrice(), paymentSuccess.postCode(),
                paymentSuccess.roadNameAddress(), paymentSuccess.detailAddress(),
                paymentSuccess.receiverPhone(), paymentSuccess.deliveryMessage()));
    }
}
