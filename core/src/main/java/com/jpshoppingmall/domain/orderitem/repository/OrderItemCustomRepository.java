package com.jpshoppingmall.domain.orderitem.repository;

import com.jpshoppingmall.domain.orderitem.dto.ProductOrderDetailDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemCustomRepository {

    ProductOrderDetailDto getOrderDetail(Long orderItemId);

    Page<ProductOrderDetailDto> getOrderDetails(Long orderId, Pageable pageable);
}
