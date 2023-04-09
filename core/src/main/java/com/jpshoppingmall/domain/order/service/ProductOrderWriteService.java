package com.jpshoppingmall.domain.order.service;

import com.jpshoppingmall.common.Address;
import com.jpshoppingmall.domain.member.entity.Member;
import com.jpshoppingmall.domain.order.entity.ProductOrder;
import com.jpshoppingmall.domain.order.repository.ProductOrderRepository;
import com.jpshoppingmall.domain.payment.dto.PaymentSuccessDto;
import com.jpshoppingmall.domain.payment.entity.Payment;
import java.time.LocalDateTime;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductOrderWriteService {

    private final ProductOrderRepository productOrderRepository;

    @Transactional
    public ProductOrder create(Member member, Payment payment, PaymentSuccessDto paymentSuccess) {
        ProductOrder productOrder = ProductOrder.builder()
            .address(
                new Address(paymentSuccess.postCode(), paymentSuccess.roadNameAddress(),
                    paymentSuccess.detailAddress()))
            .receiverPhone(paymentSuccess.receiverPhone())
            .deliveryMessage(paymentSuccess.deliveryMessage())
            .member(member)
            .payment(payment)
            .orderItems(new ArrayList<>())
            .orderedDateTime(LocalDateTime.now())
            .build();

        return productOrderRepository.save(productOrder);
    }
}
