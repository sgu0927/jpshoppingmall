package com.jpshoppingmall.domain.product.repository;

import static com.jpshoppingmall.domain.product.entity.QPurchaseCountHistory.purchaseCountHistory;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class PurchaseCountHistoryCustomRepositoryImpl implements
    PurchaseCountHistoryCustomRepository {

    private final JPAQueryFactory query;

    @Override
    public Integer getNotProcessedPurchaseCount(Long productId) {
        Integer notProcessedPurchaseCount = query
            .select(purchaseCountHistory.quantity.sum())
            .from(purchaseCountHistory)
            .where(purchaseCountHistory.productId.eq(productId))
            .fetchOne();

        return Objects.requireNonNullElse(notProcessedPurchaseCount, 0);
    }
}
