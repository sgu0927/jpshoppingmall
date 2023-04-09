package com.jpshoppingmall.domain.order.repository;

import static com.jpshoppingmall.domain.order.entity.QProductOrder.productOrder;
import static com.jpshoppingmall.domain.payment.entity.QPayment.payment;

import com.jpshoppingmall.domain.order.dto.ProductOrderDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProductOrderCustomRepositoryImpl implements ProductOrderCustomRepository {

    private final JPAQueryFactory query;

    public Page<ProductOrderDto> getMemberOrders(Long memberId, Pageable pageable) {
        List<ProductOrderDto> contents = query
            .select(Projections.constructor(ProductOrderDto.class,
                productOrder.id,
                productOrder.displayName,
                productOrder.address,
                productOrder.receiverPhone,
                productOrder.deliveryMessage,
                productOrder.payment.paidAmount,
                productOrder.orderedDateTime
            ))
            .from(productOrder)
            .join(productOrder.payment, payment)
            .where(
                productOrder.member.id.eq(memberId)
            )
            .orderBy(productOrder.orderedDateTime.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = getMemberOrderCount(memberId);

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
    }

    private JPAQuery<Long> getMemberOrderCount(Long memberId) {
        return query
            .select(productOrder.count())
            .from(productOrder)
            .where(
                productOrder.member.id.eq(memberId)
            );
    }
}
