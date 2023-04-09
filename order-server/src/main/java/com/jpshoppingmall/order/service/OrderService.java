package com.jpshoppingmall.order.service;

import com.jpshoppingmall.domain.cart.entity.Cart;
import com.jpshoppingmall.domain.cart.service.CartReadService;
import com.jpshoppingmall.domain.member.entity.Member;
import com.jpshoppingmall.domain.member.service.MemberReadService;
import com.jpshoppingmall.domain.order.entity.ProductOrder;
import com.jpshoppingmall.domain.order.service.ProductOrderWriteService;
import com.jpshoppingmall.domain.orderitem.entity.OrderItem;
import com.jpshoppingmall.domain.orderitem.service.OrderItemWriteService;
import com.jpshoppingmall.domain.payment.dto.PaymentSuccessDto;
import com.jpshoppingmall.domain.payment.entity.Payment;
import com.jpshoppingmall.domain.payment.service.PaymentReadService;
import com.jpshoppingmall.domain.product.dto.ProductCountDto;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final CartReadService cartReadService;
    private final MemberReadService memberReadService;
    private final OrderItemWriteService orderItemWriteService;
    private final PaymentReadService paymentReadService;
    private final ProductOrderWriteService productOrderWriteService;

    @Transactional
    public List<ProductCountDto> createOrder(PaymentSuccessDto paymentSuccess) {
        Map<Long, Integer> amountByCartId = paymentSuccess.amountByCartId();

        Member member = memberReadService.getMember(paymentSuccess.memberId());
        Payment payment = paymentReadService.findById(paymentSuccess.paymentId());

        ProductOrder productOrder = productOrderWriteService.create(member, payment,
            paymentSuccess);
        List<Cart> carts = cartReadService.getMemberCarts(paymentSuccess.memberId(),
            amountByCartId.keySet());

        List<OrderItem> orderItems = orderItemWriteService.saveAll(
            carts.stream().map(cart -> cart.toOrderItem(amountByCartId.get(cart.getId())))
                .toList());

        productOrder.setDisplayName(paymentSuccess.displayName());
        orderItems.forEach(productOrder::addOrderItem);
        carts.forEach(Cart::deactive);

        return orderItems.stream().map(orderItem -> new ProductCountDto(
                productOrder.getId(), orderItem.getProduct().getId(), orderItem.getItemCount()))
            .toList();
    }
}
