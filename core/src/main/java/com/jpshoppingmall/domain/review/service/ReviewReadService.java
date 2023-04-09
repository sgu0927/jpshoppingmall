package com.jpshoppingmall.domain.review.service;

import com.jpshoppingmall.domain.review.dto.ReviewResponseDto;
import com.jpshoppingmall.domain.review.entity.Review;
import com.jpshoppingmall.domain.review.repository.ReviewRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    public Page<ReviewResponseDto> getMemberReviews(Long memberId, Pageable pageable) {
        final Page<Review> reviewPage = reviewRepository.findByMemberId(memberId, pageable);
        final List<ReviewResponseDto> reviewResponseDtos = reviewPage.stream()
            .map(Review::toResponseDto)
            .toList();

        return new PageImpl<>(reviewResponseDtos, pageable, reviewPage.getTotalElements());
    }
}
