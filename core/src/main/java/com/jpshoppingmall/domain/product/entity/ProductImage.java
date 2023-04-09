package com.jpshoppingmall.domain.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jpshoppingmall.common.BaseTimeEntity;
import com.jpshoppingmall.util.BooleanToYNConverter;
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
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ProductImage extends BaseTimeEntity {
    @Id     // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 고객이 업로드한 파일명
    private String originalFileName;
    // 서버 내부에서 관리하는 파일명
    private String storeFileName;
    @Convert(converter = BooleanToYNConverter.class)
    @Column(nullable = false, length = 1)
    private Boolean isTitle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @JsonIgnore
    private Product product;
}
