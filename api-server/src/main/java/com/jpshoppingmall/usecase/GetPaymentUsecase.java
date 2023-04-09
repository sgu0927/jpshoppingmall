package com.jpshoppingmall.usecase;

import com.jpshoppingmall.domain.order.entity.ProductOrder;
import com.jpshoppingmall.domain.order.service.ProductOrderReadService;
import com.jpshoppingmall.domain.payment.dto.PaymentDto;
import com.jpshoppingmall.domain.payment.service.PaymentReadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetPaymentUsecase {

    private final ProductOrderReadService productOrderReadService;
    private final PaymentReadService paymentReadService;

    @Transactional(readOnly = true)
    public PaymentDto execute(Long orderId) {
        ProductOrder productOrder = productOrderReadService.getProductOrderById(orderId);
        return paymentReadService.findDtoById(productOrder.getPayment().getId());
    }
}
