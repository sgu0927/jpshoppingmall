package com.jpshoppingmall.domain.product.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record RegisterProductRequestDto(
    @NotNull(message = "카테고리 id를 작성해주세요")
    Long categoryId,
    @NotBlank(message = "상품명을 작성해주세요.")
    String name,
    @NotNull(message = "상품 가격을 작성해주세요.")
    Integer price,
    Integer discountPercent,
    @NotNull(message = "재고를 작성해주세요.")
    Integer totalCount
) {

}
