package com.jpshoppingmall.controller;

import com.jpshoppingmall.auth.userdetails.CustomUserDetails;
import com.jpshoppingmall.common.dto.AlertMessageDto;
import com.jpshoppingmall.domain.cart.dto.CartDto;
import com.jpshoppingmall.domain.cart.dto.CartInfoDto;
import com.jpshoppingmall.domain.cart.service.CartReadService;
import com.jpshoppingmall.domain.cart.service.CartWriteService;
import com.jpshoppingmall.domain.cart.vo.CartOrderForm;
import com.jpshoppingmall.domain.category.service.CategoryReadService;
import com.jpshoppingmall.domain.member.service.ProfileImageReadService;
import com.jpshoppingmall.domain.product.vo.AddCartForm;
import com.jpshoppingmall.exception.CommonException;
import com.jpshoppingmall.exception.CommonExceptionType;
import com.jpshoppingmall.usecase.AddCartUsecase;
import com.jpshoppingmall.usecase.DeleteCartUsecase;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CartController {

    private final AddCartUsecase addCartUsecase;
    private final CartReadService cartReadService;
    private final CartWriteService cartWriteService;
    private final CategoryReadService categoryReadService;
    private final DeleteCartUsecase deleteCartUsecase;
    private final ProfileImageReadService profileImageReadService;

    @GetMapping("/members/{memberId}/carts")
    public String myCartForm(@AuthenticationPrincipal CustomUserDetails customUser,
        @PathVariable Long memberId, Model model) {
        if (customUser == null) {
            model.addAttribute("profileImagePath", null);
            model.addAttribute("memberId",null);
            // 예외처리
        } else {
            model.addAttribute("profileImagePath",
                profileImageReadService.getMemberProfileImagePath(customUser.getMemberId()));
            model.addAttribute("memberId",customUser.getMemberId());
        }

        model.addAttribute("cartOrderForm", new CartOrderForm());
        model.addAttribute("myCarts", cartReadService.getActiveMemberCartsProduct(memberId));
        model.addAttribute("categoryList", categoryReadService.getCategories());
        return "cart/my-cart";
    }

    @PostMapping(value = "/members/{memberId}/carts")
    public String addCart(
        @PathVariable Long memberId,
        @RequestParam(value = "productId") Long productId,
        Model model,
        @ModelAttribute AddCartForm addCartForm,
        @AuthenticationPrincipal CustomUserDetails customUser) {

        if (customUser == null) {
            AlertMessageDto message = new AlertMessageDto("로그인이 필요한 서비스입니다", "/login",
                RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        } else if (!Objects.equals(customUser.getMemberId(), memberId)) {
            throw new CommonException(CommonExceptionType.INVALID_PARAMS, "잘못된 memberId 접근입니다.");
        }

        try {
            addCartUsecase.execute(memberId, new CartDto(addCartForm.getCount(), productId));
        } catch (Exception e) {
            e.printStackTrace();
            AlertMessageDto message = new AlertMessageDto("요청이 실패했습니다. 실패가 계속되면 관리자에게 문의 바랍니다.",
                "/product-detail/" + productId.toString(),
                RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }

        AlertMessageDto message = new AlertMessageDto("장바구니에 성공적으로 담겼습니다!",
            "/members/" + memberId.toString() + "/carts",
            RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }

    @Operation(summary = "장바구니 정보 조회")
    @ResponseBody
    @GetMapping("/members/{memberId}/cart-info")
    public List<CartInfoDto> getCartInfoList(@PathVariable Long memberId,
        @RequestParam(value = "cartIds[]", required = true) List<Long> cartIds) {
        return cartReadService.getCartInfoList(memberId, cartIds);
    }

    @Operation(summary = "장바구니 상품 개수 변경")
    @PutMapping("/members/{memberId}/carts/{cartId}")
    public CartDto updateCartItemCount(@PathVariable Long memberId,
        @PathVariable Long cartId, @RequestBody CartDto cartDto) {
        return cartWriteService.updateCartItemCount(cartId, cartDto.itemCount());
    }

    @Operation(summary = "장바구니 삭제")
    @ResponseBody
    @DeleteMapping("/members/{memberId}/carts/{cartId}")
    public void deleteCart(
        @AuthenticationPrincipal CustomUserDetails customUser,
        @PathVariable("memberId") Long memberId,
        @PathVariable("cartId") Long cartId
    ) {
        if (customUser == null) {
            throw new CommonException(CommonExceptionType.INVALID_PARAMS, "잘못된 memberId 접근입니다.");
        } else if (!Objects.equals(customUser.getMemberId(), memberId)) {
            throw new CommonException(CommonExceptionType.INVALID_PARAMS, "잘못된 memberId 접근입니다.");
        }

        deleteCartUsecase.execute(memberId, cartId);
    }

    // 사용자에게 메시지를 전달하고, 페이지를 리다이렉트 한다.
    private String showMessageAndRedirect(final AlertMessageDto params, Model model) {
        model.addAttribute("params", params);
        return "common/messageRedirect";
    }
}
