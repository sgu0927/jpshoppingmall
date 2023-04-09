package com.jpshoppingmall.domain.orderitem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jpshoppingmall.common.BaseTimeEntity;
import com.jpshoppingmall.domain.order.entity.ProductOrder;
import com.jpshoppingmall.domain.product.entity.Product;
import com.jpshoppingmall.type.EnumMaster.OrderStatus;
import com.jpshoppingmall.util.BooleanToYNConverter;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class OrderItem extends BaseTimeEntity {

    @Id     // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_order_id", referencedColumnName = "id")
    private ProductOrder productOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    // 주문 시점 가격(할인, 쿠폰 등 다 포함)
    private Integer orderPrice;
    private Integer itemCount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(nullable = false, length = 1)
    private Boolean hasReview;

    private LocalDateTime deliveredDateTime;

    public void changeHasReviewState(boolean to) {
        hasReview = to;
    }

    public void cancel() {
        verifyOrderBeforeShipped();
        this.status = OrderStatus.CANCELED;
    }

    private void verifyOrderBeforeShipped() {
        if (this.status != OrderStatus.PAYMENT_WAITING && this.status != OrderStatus.PREPARING) {
            throw new RuntimeException("에러");
        }
    }
}
