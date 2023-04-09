package com.jpshoppingmall.controller;

import com.jpshoppingmall.auth.userdetails.CustomUserDetails;
import com.jpshoppingmall.common.dto.AlertMessageDto;
import com.jpshoppingmall.domain.category.service.CategoryReadService;
import com.jpshoppingmall.domain.member.service.ProfileImageReadService;
import com.jpshoppingmall.domain.orderitem.dto.ProductOrderDetailDto;
import com.jpshoppingmall.domain.orderitem.service.OrderItemReadService;
import com.jpshoppingmall.domain.product.vo.AddCartForm;
import com.jpshoppingmall.domain.review.vo.CreateReviewForm;
import com.jpshoppingmall.exception.CommonException;
import com.jpshoppingmall.exception.CommonExceptionType;
import com.jpshoppingmall.service.PurchaseCountService;
import com.jpshoppingmall.type.EnumMaster.PagingType;
import com.jpshoppingmall.usecase.CreateReviewUsecase;
import com.jpshoppingmall.usecase.GetProductDetailUsecase;
import com.jpshoppingmall.usecase.GetProductReviewUsecase;
import com.jpshoppingmall.util.PageRequestUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ReviewController {

    private final CategoryReadService categoryReadService;
    private final CreateReviewUsecase createReviewUsecase;
    private final GetProductDetailUsecase getProductDetailUsecase;
    private final GetProductReviewUsecase getProductReviewUsecase;
    private final ProfileImageReadService profileImageReadService;
    private final OrderItemReadService orderItemReadService;
    private final PurchaseCountService purchaseCountService;

    @GetMapping("/products/{productId}/review-form")
    public String reviewForm(
        @AuthenticationPrincipal CustomUserDetails customUser,
        @PathVariable Long productId,
        @RequestParam Long orderItemId,
        Model model
    ) {
        if (customUser == null) {
            model.addAttribute("profileImagePath", null);
            model.addAttribute("memberId", null);
        } else {
            model.addAttribute("profileImagePath",
                profileImageReadService.getMemberProfileImagePath(customUser.getMemberId()));
            model.addAttribute("memberId", customUser.getMemberId());
        }

        ProductOrderDetailDto orderDetailDto = orderItemReadService.getOrderDetail(orderItemId);
        if (orderDetailDto.hasReview()) {
            throw new CommonException(CommonExceptionType.ALREADY_HAS_REVIEW);
        }

        model.addAttribute("categoryList", categoryReadService.getCategories());
        model.addAttribute("reviewForm", new CreateReviewForm());
        model.addAttribute("orderDetail", orderDetailDto);

        return "review/review-form";
    }

    @Operation(summary = "상품 리뷰 추가")
    @PostMapping("/products/{productId}/reviews")
    public String addReview(
        @AuthenticationPrincipal CustomUserDetails customUser,
        @Parameter(name = "productId", description = "상품 id") @PathVariable final Long productId,
        @RequestParam final Long orderItemId,
        @ModelAttribute CreateReviewForm reviewForm,
        Model model) {

        if (customUser == null) {
            AlertMessageDto message = new AlertMessageDto("로그인이 필요한 서비스입니다.",
                "/login", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }

        try {
            createReviewUsecase.execute(productId, customUser.getMemberId(), orderItemId,
                reviewForm);
        } catch (Exception e) {
            e.printStackTrace();
            AlertMessageDto message = new AlertMessageDto("리뷰 등록에 실패했습니다. 실패가 계속되면 관리자에게 문의 바랍니다.",
                "/products/" + productId.toString() + "/review-form",
                RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }

        AlertMessageDto message = new AlertMessageDto("리뷰가 성공적으로 등록되었습니다!",
            "/members/" + customUser.getMemberId().toString() + "/orders",
            RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }

    @Operation(summary = "상품 리뷰 조회")
    @GetMapping("/products/{productId}/reviews")
    public String productReviews(
        @AuthenticationPrincipal CustomUserDetails customUser,
        @PathVariable Long productId,
        @Parameter(name = "pageRequest", description = "페이징 정보") final PageRequestUtil pageRequest,
        Model model
    ) {
        if (customUser == null) {
            model.addAttribute("profileImagePath", null);
            model.addAttribute("memberId", null);
        } else {
            model.addAttribute("profileImagePath",
                profileImageReadService.getMemberProfileImagePath(customUser.getMemberId()));
            model.addAttribute("memberId", customUser.getMemberId());
        }

        model.addAttribute("addCartForm", new AddCartForm());
        model.addAttribute("categoryList", categoryReadService.getCategories());
        model.addAttribute("productDetail", getProductDetailUsecase.execute(productId));
        model.addAttribute("purchaseCount",
            purchaseCountService.getCurrentPurchaseCount(productId));

        model.addAttribute("reviews", getProductReviewUsecase.execute(productId, pageRequest.of(
            PagingType.REVIEW)));

        return "review/product-review";
    }

    private String showMessageAndRedirect(final AlertMessageDto params, Model model) {
        model.addAttribute("params", params);
        return "common/messageRedirect";
    }
}
