package com.jpshoppingmall.usecase;

import com.jpshoppingmall.domain.member.entity.Member;
import com.jpshoppingmall.domain.member.service.MemberReadService;
import com.jpshoppingmall.domain.orderitem.service.OrderItemWriteService;
import com.jpshoppingmall.domain.product.entity.Product;
import com.jpshoppingmall.domain.product.service.ProductReadService;
import com.jpshoppingmall.domain.review.service.ReviewWriteService;
import com.jpshoppingmall.domain.review.vo.CreateReviewForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CreateReviewUsecase {

    private final MemberReadService memberReadService;
    private final ProductReadService productReadService;
    private final ReviewWriteService reviewWriteService;
    private final OrderItemWriteService orderItemWriteService;

    @Transactional
    public void execute(Long productId, Long memberId, Long orderItemId,
        CreateReviewForm createReviewForm) {
        // member 존재하는지 확인
        Member member = memberReadService.getMember(memberId);
        Product product = productReadService.getProduct(productId);
        reviewWriteService.addReview(member, product, createReviewForm);
        product.updateRating(createReviewForm.getRating());
        orderItemWriteService.updateOrderItemHasReview(orderItemId, true);
    }
}
