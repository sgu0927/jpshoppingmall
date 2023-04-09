package com.jpshoppingmall.domain.cart.repository;

import com.jpshoppingmall.domain.cart.dto.CartInfoDto;
import com.jpshoppingmall.domain.cart.dto.CartProductDto;
import com.jpshoppingmall.domain.cart.entity.Cart;
import java.util.List;
import java.util.Set;

public interface CartCustomRepository {
    List<Cart> getMemberCarts(Long memberId, Set<Long> cartIds);

    List<CartInfoDto> getCartInfoList(Long memberId, List<Long> cartIds);
    List<CartProductDto> getActiveMemberCartProducts(Long memberId);
}
