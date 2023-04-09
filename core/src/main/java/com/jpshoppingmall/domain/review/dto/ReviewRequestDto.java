package com.jpshoppingmall.domain.review.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

public record ReviewRequestDto(
    @NotNull
    Long memberId,
    @Range(min = 1L, max = 5L)
    Float rating,
    @NotBlank
    @Size(min = 10, max = 200)
    String comment
) {

}
