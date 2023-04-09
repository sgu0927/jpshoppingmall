package com.jpshoppingmall.domain.cart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jpshoppingmall.common.BaseTimeEntity;
import com.jpshoppingmall.domain.member.entity.Member;
import com.jpshoppingmall.domain.orderitem.entity.OrderItem;
import com.jpshoppingmall.domain.product.entity.Product;
import com.jpshoppingmall.type.EnumMaster.OrderStatus;
import com.jpshoppingmall.util.BooleanToYNConverter;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Cart extends BaseTimeEntity {

    @Id     // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer itemCount;

    @Setter
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(nullable = false, length = 1)
    private Boolean isActive;

    public OrderItem toOrderItem(Integer orderPrice) {
        return OrderItem.builder()
            .product(product)
            .itemCount(itemCount)
            .orderPrice(orderPrice)
            .status(OrderStatus.PREPARING)
            .hasReview(false)
            .createdDateTime(LocalDateTime.now())
            .modifiedDateTime(LocalDateTime.now())
            .build();
    }

    public void changeItemCount(Integer to) {
        if (to <= 0) {
            throw new RuntimeException("에러");
        }
        itemCount = to;
    }

    public void deactive() {
        this.isActive = false;
    }
}
