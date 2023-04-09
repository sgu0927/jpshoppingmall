package com.jpshoppingmall.domain.product.repository;

import com.jpshoppingmall.domain.product.entity.PurchaseCount;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseCountRepository extends JpaRepository<PurchaseCount, Long> {

    Optional<PurchaseCount> findByProductId(Long productId);

    List<PurchaseCount> findAllByProductIdIn(List<Long> productIds);
}
