package com.jpshoppingmall.usecase;

import com.jpshoppingmall.domain.cart.entity.Cart;
import com.jpshoppingmall.domain.cart.service.CartReadService;
import com.jpshoppingmall.domain.cart.service.CartWriteService;
import com.jpshoppingmall.domain.member.entity.Member;
import com.jpshoppingmall.domain.member.service.MemberReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DeleteCartUsecase {

    private final MemberReadService memberReadService;
    private final CartReadService cartReadService;
    private final CartWriteService cartWriteService;

    @Transactional
    public void execute(Long memberId, Long cartId) {
        Member member = memberReadService.getMember(memberId);
        // cart 존재 체크
        Cart cart = cartReadService.getCartById(cartId);
        member.getCarts().remove(cart);
        cartWriteService.deleteCart(cartId);
    }
}
