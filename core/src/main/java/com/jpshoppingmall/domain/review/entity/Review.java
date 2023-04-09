package com.jpshoppingmall.domain.review.entity;

import com.jpshoppingmall.common.BaseTimeEntity;
import com.jpshoppingmall.domain.member.entity.Member;
import com.jpshoppingmall.domain.product.entity.Product;
import com.jpshoppingmall.domain.review.dto.ReviewResponseDto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Range;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 200)
    @Size(min = 10, max = 200)
    private String comment;

    @Range(min = 1, max = 5)
    private Integer rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;


    public ReviewResponseDto toResponseDto() {
        return new ReviewResponseDto(id, null, member.getNickname(), comment, rating,
            getCreatedDateTime());
    }
}
