package com.jpshoppingmall.domain.category.dto;

public record CategoryDto(
    Long id,
    String categoryName,
    Long parentCategoryId,
    Boolean hasChild
) {

}
