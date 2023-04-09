package com.jpshoppingmall.domain.review.service;

import com.jpshoppingmall.domain.member.entity.Member;
import com.jpshoppingmall.domain.product.entity.Product;
import com.jpshoppingmall.domain.review.entity.Review;
import com.jpshoppingmall.domain.review.repository.ReviewRepository;
import com.jpshoppingmall.domain.review.vo.CreateReviewForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReviewWriteService {

    private final ReviewRepository reviewRepository;

    @Transactional
    public void addReview(Member member, Product product, CreateReviewForm createReviewForm) {
        final Review review = Review.builder()
            .comment(createReviewForm.getComment())
            .rating(createReviewForm.getRating())
            .member(member)
            .product(product)
            .build();

        var savedReview = reviewRepository.save(review);
    }
}
