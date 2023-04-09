package com.jpshoppingmall.domain.orderitem.repository;

import static com.jpshoppingmall.domain.orderitem.entity.QOrderItem.orderItem;
import static com.jpshoppingmall.domain.product.entity.QProduct.product;

import com.jpshoppingmall.domain.orderitem.dto.ProductOrderDetailDto;
import com.jpshoppingmall.domain.orderitem.entity.OrderItem;
import com.jpshoppingmall.domain.product.entity.Product;
import com.jpshoppingmall.domain.product.entity.ProductImage;
import com.jpshoppingmall.exception.CommonException;
import com.jpshoppingmall.exception.CommonExceptionType;
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
public class OrderItemCustomRepositoryImpl implements OrderItemCustomRepository {

    private final JPAQueryFactory query;

    public ProductOrderDetailDto getOrderDetail(Long orderItemId) {
        OrderItem foundOrderItem = query
            .selectFrom(orderItem)
            .join(orderItem.product, product).fetchJoin()
            .where(
                orderItem.id.eq(orderItemId)
            )
            .fetchOne();

        if (foundOrderItem == null) {
            throw new CommonException(CommonExceptionType.CANNOT_FOUND_ORDER_ITEM);
        }
        if (foundOrderItem.getProduct() == null) {
            throw new CommonException(CommonExceptionType.CANNOT_FOUND_PRODUCT);
        }

        Product orderedProduct = foundOrderItem.getProduct();
        ProductImage productImage = orderedProduct.getProductImageList().stream()
            .filter(ProductImage::getIsTitle).findAny()
            .orElseThrow(() -> new CommonException(CommonExceptionType.CANNOT_FOUND_PRODUCT_IMAGE));

        return new ProductOrderDetailDto(foundOrderItem.getId(),
            foundOrderItem.getProduct().getId(), foundOrderItem.getProduct().getName(),
            foundOrderItem.getItemCount(), foundOrderItem.getOrderPrice(),
            productImage.getStoreFileName(), foundOrderItem.getStatus(),
            foundOrderItem.getHasReview(), foundOrderItem.getDeliveredDateTime());
    }

    public Page<ProductOrderDetailDto> getOrderDetails(Long orderId, Pageable pageable) {
        List<OrderItem> orderItems = query
            .selectFrom(orderItem)
            .leftJoin(orderItem.product, product)
            .where(
                orderItem.productOrder.id.eq(orderId)
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        // TODO : N+1 개선
        List<ProductOrderDetailDto> contents = orderItems.stream().map(orderItem1 -> {
            Product product1 = orderItem1.getProduct();
            ProductImage productImage = product1.getProductImageList().stream()
                .filter(ProductImage::getIsTitle).findAny().orElseThrow(() -> new CommonException(
                    CommonExceptionType.CANNOT_FOUND_PRODUCT_IMAGE));

            return new ProductOrderDetailDto(orderItem1.getId(), product1.getId(),
                product1.getName(),
                orderItem1.getItemCount(), orderItem1.getOrderPrice(),
                productImage.getStoreFileName(), orderItem1.getStatus(),
                orderItem1.getHasReview(),
                orderItem1.getDeliveredDateTime());
        }).toList();

        JPAQuery<Long> countQuery = getOrderItemCount(orderId);

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
    }

    private JPAQuery<Long> getOrderItemCount(Long orderId) {
        return query.select(orderItem.count())
            .from(orderItem)
            .leftJoin(orderItem.product, product)
            .where(
                orderItem.productOrder.id.eq(orderId)
            );
    }
}
