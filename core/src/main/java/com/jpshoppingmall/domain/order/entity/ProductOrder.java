package com.jpshoppingmall.domain.order.entity;

import com.jpshoppingmall.common.Address;
import com.jpshoppingmall.domain.member.entity.Member;
import com.jpshoppingmall.domain.orderitem.entity.OrderItem;
import com.jpshoppingmall.domain.payment.entity.Payment;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ProductOrder {

    @Id     // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 조회용 이름, ex) XXXXX 외 4종
    private String displayName;
    private String orderNumber;
    private String deliveryMessage;

    @Embedded
    private Address address;

    // 받는 사람 연락처
    private String receiverPhone;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    private Payment payment;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "productOrder", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    /**
     * 시스템 상에서는 회원, 주문을 동급으로 놓고 생각해야. 주문이 회원을 통해 일어나는게 아니라 주문을 생성할 때 회원이 필요하다고 보는게 더 맞다. 실무에서는 주문이
     * 회원을 참조하는 것으로 충분
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    private LocalDateTime orderedDateTime;

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setProductOrder(this);
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
