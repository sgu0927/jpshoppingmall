package com.jpshoppingmall.domain.cart.service;

import com.jpshoppingmall.domain.cart.dto.CartDto;
import com.jpshoppingmall.domain.cart.entity.Cart;
import com.jpshoppingmall.domain.cart.repository.CartRepository;
import com.jpshoppingmall.domain.member.entity.Member;
import com.jpshoppingmall.domain.product.entity.Product;
import com.jpshoppingmall.exception.CommonException;
import com.jpshoppingmall.exception.CommonExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CartWriteService {

    private final CartRepository cartRepository;

    @Transactional
    public Cart addCart(Member member, Product product, Integer itemCount) {
        return cartRepository.save(Cart.builder()
            .itemCount(itemCount)
            .member(member)
            .product(product)
            .isActive(true)
            .build());
    }

    @Transactional
    public CartDto updateCartItemCount(Long cartId, Integer itemCount) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new CommonException(
            CommonExceptionType.CANNOT_FOUND_CART));
        cart.changeItemCount(itemCount);
        return new CartDto(cart.getItemCount(), null);
    }

    // 만료된 것 삭제는 createdDateTime 기준 7일이 지나면 삭제
    @Transactional
    public void deleteCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new CommonException(
            CommonExceptionType.CANNOT_FOUND_CART));
        cart.deactive();
        // cartRepository.deleteById(cartId);
    }
}
