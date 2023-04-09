package com.jpshoppingmall.domain.review.dto;

import java.time.LocalDateTime;

public record ReviewResponseDto(
    Long id,
    String profileImagePath,
    String nickname,
    String comment,
    Integer rating,
    LocalDateTime createdDateTime
) {

}
