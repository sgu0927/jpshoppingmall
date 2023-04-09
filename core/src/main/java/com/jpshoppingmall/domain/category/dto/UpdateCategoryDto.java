package com.jpshoppingmall.domain.category.dto;

import javax.validation.constraints.NotBlank;

public record UpdateCategoryDto(
    @NotBlank(message = "카테고리명을 작성하세요.")
    String categoryName,
    Long parentCategoryId
) {

}
