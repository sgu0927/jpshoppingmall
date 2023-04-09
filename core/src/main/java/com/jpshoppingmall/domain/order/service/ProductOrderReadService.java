package com.jpshoppingmall.domain.order.service;

import com.jpshoppingmall.domain.order.dto.ProductOrderDto;
import com.jpshoppingmall.domain.order.entity.ProductOrder;
import com.jpshoppingmall.domain.order.repository.ProductOrderRepository;
import com.jpshoppingmall.exception.CommonException;
import com.jpshoppingmall.exception.CommonExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductOrderReadService {

    private final ProductOrderRepository productOrderRepository;

    @Transactional(readOnly = true)
    public ProductOrder getProductOrderById(Long orderId) {
        return productOrderRepository.findById(orderId).orElseThrow(() -> new CommonException(
            CommonExceptionType.CANNOT_FOUND_ORDER));
    }

    @Transactional(readOnly = true)
    public Page<ProductOrderDto> getMemberOrders(Long memberId, Pageable pageable) {
        return productOrderRepository.getMemberOrders(memberId, pageable);
    }
}
