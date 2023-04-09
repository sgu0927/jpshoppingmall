package com.jpshoppingmall.domain.order.repository;

import com.jpshoppingmall.domain.order.entity.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long>, ProductOrderCustomRepository {

}
