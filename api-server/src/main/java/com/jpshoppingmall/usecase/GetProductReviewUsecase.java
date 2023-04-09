package com.jpshoppingmall.usecase;

import com.jpshoppingmall.domain.member.entity.Member;
import com.jpshoppingmall.domain.member.service.ProfileImageReadService;
import com.jpshoppingmall.domain.review.dto.ReviewResponseDto;
import com.jpshoppingmall.domain.review.entity.Review;
import com.jpshoppingmall.domain.review.service.ReviewReadService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetProductReviewUsecase {

    private final ProfileImageReadService profileImageReadService;
    private final ReviewReadService reviewReadService;

    public Page<ReviewResponseDto> execute(Long productId, Pageable pageable) {
        Page<Review> reviewPage = reviewReadService.getProductReviews(productId, pageable);
        List<ReviewResponseDto> reviewResponseDtos = reviewPage.getContent().stream()
            .map(review -> {
                Member member = review.getMember();
                String profileImagePath = profileImageReadService.getMemberProfileImagePath(
                    member.getId());
                return new ReviewResponseDto(review.getId(), profileImagePath, member.getNickname(),
                    review.getComment(), review.getRating(), review.getCreatedDateTime());
            }).toList();

        return new PageImpl<>(reviewResponseDtos, pageable, reviewPage.getTotalElements());
    }
}
