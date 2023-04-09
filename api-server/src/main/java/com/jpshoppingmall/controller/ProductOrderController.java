package com.jpshoppingmall.controller;

import com.jpshoppingmall.auth.userdetails.CustomUserDetails;
import com.jpshoppingmall.common.dto.AlertMessageDto;
import com.jpshoppingmall.domain.category.service.CategoryReadService;
import com.jpshoppingmall.domain.member.service.ProfileImageReadService;
import com.jpshoppingmall.domain.order.dto.ProductOrderDto;
import com.jpshoppingmall.domain.order.service.ProductOrderReadService;
import com.jpshoppingmall.domain.orderitem.service.OrderItemReadService;
import com.jpshoppingmall.domain.payment.dto.PaymentSuccessDto;
import com.jpshoppingmall.exception.CommonException;
import com.jpshoppingmall.exception.CommonExceptionType;
import com.jpshoppingmall.type.EnumMaster.PagingType;
import com.jpshoppingmall.usecase.CreatePaymentUsecase;
import com.jpshoppingmall.usecase.GetPaymentUsecase;
import com.jpshoppingmall.util.PageRequestUtil;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductOrderController {

    private final CategoryReadService categoryReadService;
    private final CreatePaymentUsecase createPaymentUsecase;
    private final GetPaymentUsecase getPaymentUsecase;
    private final ProductOrderReadService productOrderReadService;
    private final ProfileImageReadService profileImageReadService;
    private final OrderItemReadService orderItemReadService;

    @GetMapping("/members/{memberId}/orders")
    public String memberOrders(
        @AuthenticationPrincipal CustomUserDetails customUser,
        @Parameter(name = "memberId", description = "멤버 id") @PathVariable Long memberId,
        @Parameter(name = "pageRequest", description = "페이징 정보") final PageRequestUtil pageRequest,
        Model model
    ) {
        if (!Objects.equals(customUser.getMemberId(), memberId)) {
            throw new CommonException(CommonExceptionType.INVALID_PARAMS, "잘못된 memberId 접근입니다.");
        }

        Page<ProductOrderDto> productOrderDtos = productOrderReadService.getMemberOrders(memberId,
            pageRequest.of(PagingType.ORDER));

        model.addAttribute("orders", productOrderDtos);
        model.addAttribute("categoryList", categoryReadService.getCategories());
        model.addAttribute("profileImagePath",
            profileImageReadService.getMemberProfileImagePath(customUser.getMemberId()));

        return "order/my-order";
    }

    @GetMapping("/members/{memberId}/orders/{orderId}")
    public String orderDetail(
        @AuthenticationPrincipal CustomUserDetails customUser,
        @Parameter(name = "memberId", description = "멤버 id") @PathVariable Long memberId,
        @Parameter(name = "orderId", description = "주문 id") @PathVariable Long orderId,
        @Parameter(name = "pageRequest", description = "페이징 정보") final PageRequestUtil pageRequest,
        Model model
    ) {
        if (!Objects.equals(customUser.getMemberId(), memberId)) {
            throw new CommonException(CommonExceptionType.INVALID_PARAMS, "잘못된 memberId 접근입니다.");
        }

        model.addAttribute("memberId", memberId);
        model.addAttribute("payment", getPaymentUsecase.execute(orderId));
        model.addAttribute("orderItems", orderItemReadService.getOrderDetails(
            orderId, pageRequest.of(PagingType.ORDER_DETAIL)));
        model.addAttribute("categoryList", categoryReadService.getCategories());
        model.addAttribute("profileImagePath",
            profileImageReadService.getMemberProfileImagePath(customUser.getMemberId()));

        return "order/order-detail";
    }

    @PostMapping("/payment")
    public String createPayment(
        @AuthenticationPrincipal CustomUserDetails customUser,
        @RequestBody PaymentSuccessDto paymentSuccess,
        Model model
    ) {
        log.info("[ProductOrderController] paymentSuccess :: {}", paymentSuccess.toString());

        if (customUser == null) {
            AlertMessageDto message = new AlertMessageDto("customUser가 null입니다.",
                "/login", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }

        createPaymentUsecase.execute(paymentSuccess);

        AlertMessageDto message = new AlertMessageDto("결제가 완료되었습니다.",
            "/", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }

    private String showMessageAndRedirect(final AlertMessageDto params, Model model) {
        model.addAttribute("params", params);
        return "common/messageRedirect";
    }
}
