package com.jpshoppingmall.domain.orderitem.service;

import com.jpshoppingmall.domain.orderitem.dto.UpdateOrderItemDto;
import com.jpshoppingmall.domain.orderitem.entity.OrderItem;
import com.jpshoppingmall.domain.orderitem.repository.OrderItemRepository;
import com.jpshoppingmall.exception.CommonException;
import com.jpshoppingmall.exception.CommonExceptionType;
import com.jpshoppingmall.type.EnumMaster.OrderStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderItemWriteService {

    private final OrderItemRepository orderItemRepository;

    @Transactional
    public List<OrderItem> saveAll(List<OrderItem> orderItems) {
        return orderItemRepository.saveAll(orderItems);
    }

    @Transactional
    public void updateOrderItemHasReview(Long orderItemId, Boolean state) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
            .orElseThrow(() -> new CommonException(CommonExceptionType.CANNOT_FOUND_ORDER_ITEM));
        orderItem.changeHasReviewState(state);
    }

    @Transactional
    public void update(Long orderItemId, UpdateOrderItemDto updateOrderItemDto) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(() ->
            new CommonException(CommonExceptionType.CANNOT_FOUND_ORDER_ITEM));
        OrderStatus toStatus = OrderStatus.valueOf(updateOrderItemDto.status());
//        switch (toStatus) {
//            case CANCELED -> {
//                orderItem.cancel();
//                // 환불 진행
//            }
//            case
//        }
    }
}
