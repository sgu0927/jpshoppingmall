package com.jpshoppingmall.domain.cart.service;

import com.jpshoppingmall.domain.cart.dto.CartInfoDto;
import com.jpshoppingmall.domain.cart.dto.CartProductDto;
import com.jpshoppingmall.domain.cart.entity.Cart;
import com.jpshoppingmall.domain.cart.repository.CartRepository;
import com.jpshoppingmall.exception.CommonException;
import com.jpshoppingmall.exception.CommonExceptionType;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CartReadService {

    private final CartRepository cartRepository;

    @Transactional(readOnly = true)
    public Cart getCartById(Long cartId) {
        return cartRepository.findById(cartId)
            .orElseThrow(() -> new CommonException(CommonExceptionType.CANNOT_FOUND_CART));
    }

    @Transactional(readOnly = true)
    public List<Cart> getMemberCarts(Long memberId, Set<Long> cartIds) {
        return cartRepository.getMemberCarts(memberId, cartIds);
    }

    @Transactional(readOnly = true)
    public List<CartInfoDto> getCartInfoList(Long memberId, List<Long> cartIds) {
        return cartRepository.getCartInfoList(memberId, cartIds);
    }

    @Transactional(readOnly = true)
    public List<CartProductDto> getActiveMemberCartsProduct(Long memberId) {
        return cartRepository.getActiveMemberCartProducts(memberId);
    }
}
