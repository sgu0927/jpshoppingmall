package com.jpshoppingmall.domain.orderitem.service;

import com.jpshoppingmall.domain.orderitem.dto.ProductOrderDetailDto;
import com.jpshoppingmall.domain.orderitem.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderItemReadService {
    private final OrderItemRepository orderItemRepository;

    @Transactional(readOnly = true)
    public ProductOrderDetailDto getOrderDetail(Long orderItemId) {
        return orderItemRepository.getOrderDetail(orderItemId);
    }

    @Transactional(readOnly = true)
    public Page<ProductOrderDetailDto> getOrderDetails(Long orderId, Pageable pageable) {
        return orderItemRepository.getOrderDetails(orderId, pageable);
    }
}
