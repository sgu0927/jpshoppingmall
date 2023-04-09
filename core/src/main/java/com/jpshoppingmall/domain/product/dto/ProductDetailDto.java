package com.jpshoppingmall.domain.product.dto;

import com.jpshoppingmall.domain.product.entity.ProductImage;
import com.jpshoppingmall.type.EnumMaster.ProductStatus;
import java.util.List;

public record ProductDetailDto(
    Long id,
    String name,
    Integer price,
    Integer discountPercent,
    Float rating,
    ProductStatus productStatus,
    List<ProductImage> productImageList
) {

}
