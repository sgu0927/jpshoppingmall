package com.jpshoppingmall.domain.order.repository;

import com.jpshoppingmall.domain.order.dto.ProductOrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOrderCustomRepository {
    Page<ProductOrderDto> getMemberOrders(Long memberId, Pageable pageable);
}
