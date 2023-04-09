package com.jpshoppingmall.usecase;

import com.jpshoppingmall.domain.cart.dto.CartDto;
import com.jpshoppingmall.domain.cart.entity.Cart;
import com.jpshoppingmall.domain.cart.service.CartWriteService;
import com.jpshoppingmall.domain.member.entity.Member;
import com.jpshoppingmall.domain.member.service.MemberReadService;
import com.jpshoppingmall.domain.product.entity.Product;
import com.jpshoppingmall.domain.product.service.ProductReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AddCartUsecase {

    private final MemberReadService memberReadService;
    private final ProductReadService productReadService;
    private final CartWriteService cartWriteService;

    @Transactional
    public CartDto execute(Long memberId, CartDto cartDto) {
        Member member = memberReadService.getMember(memberId);
        Product product = productReadService.getProduct(cartDto.productId());
        Cart cart = cartWriteService.addCart(member, product, cartDto.itemCount());
        member.addCart(cart);
        return new CartDto(cart.getItemCount(), cartDto.productId());
    }
}
