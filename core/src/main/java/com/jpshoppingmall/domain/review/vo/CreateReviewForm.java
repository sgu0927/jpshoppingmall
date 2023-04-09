package com.jpshoppingmall.domain.review.vo;

import lombok.Data;

@Data
public class CreateReviewForm {
    private Integer rating;
    private String comment;
}
