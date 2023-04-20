package com.jpshoppingmall.domain.review.service;

import com.jpshoppingmall.domain.review.entity.Review;
import com.jpshoppingmall.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReviewReadService {

    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public Page<Review> getProductReviews(Long productId, Pageable pageable) {
        return reviewRepository.findByProductId(productId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Review> getMemberReviews(Long memberId, Pageable pageable) {
        return reviewRepository.findByMemberId(memberId, pageable);
    }
}
