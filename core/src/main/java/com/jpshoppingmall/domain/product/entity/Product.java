package com.jpshoppingmall.domain.product.entity;

import com.jpshoppingmall.common.BaseTimeEntity;
import com.jpshoppingmall.domain.category.entity.Category;
import com.jpshoppingmall.domain.product.dto.ProductDetailDto;
import com.jpshoppingmall.domain.product.dto.RegisterProductResponseDto;
import com.jpshoppingmall.type.EnumMaster.ProductStatus;
import com.jpshoppingmall.util.BooleanToYNConverter;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Product extends BaseTimeEntity {

    @Id     // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;
    private String name;
    private Integer price;
    private Integer discountPercent;
    private Integer totalCount;
    private Integer reviewCount;
    private Float rating;
    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ProductImage> productImageList = new ArrayList<>();

    @Convert(converter = BooleanToYNConverter.class)
    @Column(nullable = false, length = 1)
    private Boolean isConfirm;

    public RegisterProductResponseDto toResponseDto() {
        return new RegisterProductResponseDto(name, price, discountPercent, totalCount);
    }

    public ProductDetailDto toDetailDto() {
        return new ProductDetailDto(id, name, price, discountPercent, rating,
            productStatus, productImageList);
    }

    public void updateRating(Integer num) {
        float currentNum = rating * reviewCount.floatValue();
        reviewCount++;
        rating = (currentNum + num) / reviewCount.floatValue();
    }
}
