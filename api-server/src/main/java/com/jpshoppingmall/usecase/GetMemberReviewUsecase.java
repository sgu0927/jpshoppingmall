package com.jpshoppingmall.usecase;

import com.jpshoppingmall.domain.product.entity.Product;
import com.jpshoppingmall.domain.product.entity.ProductImage;
import com.jpshoppingmall.domain.product.service.ProductReadService;
import com.jpshoppingmall.domain.review.dto.ReviewResponseDto;
import com.jpshoppingmall.domain.review.entity.Review;
import com.jpshoppingmall.domain.review.service.ReviewReadService;
import com.jpshoppingmall.exception.CommonException;
import com.jpshoppingmall.exception.CommonExceptionType;
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
public class GetMemberReviewUsecase {

    private final ReviewReadService reviewReadService;
    private final ProductReadService productReadService;

    // TODO : N+1 문제 해결
    public Page<ReviewResponseDto> execute(Long productId, Pageable pageable) {
        Page<Review> reviewPage = reviewReadService.getMemberReviews(productId, pageable);
        List<ReviewResponseDto> reviewResponseDtos = reviewPage.getContent().stream()
            .map(review -> {
                Product product = productReadService.getProductWithImages(
                    review.getProduct().getId());
                String productTitleImagePath = product.getProductImageList().stream()
                    .filter(ProductImage::getIsTitle).findAny()
                    .orElseThrow(() -> new CommonException(
                        CommonExceptionType.CANNOT_FOUND_PRODUCT_IMAGE)).getStoreFileName();
                return new ReviewResponseDto(review.getId(), product.getId(), product.getName(),
                    productTitleImagePath, null, null, review.getComment(),
                    review.getRating(), review.getCreatedDateTime());
            }).toList();

        return new PageImpl<>(reviewResponseDtos, pageable, reviewPage.getTotalElements());
    }
}
