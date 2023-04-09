package com.jpshoppingmall.domain.cart.repository;

import static com.jpshoppingmall.domain.cart.entity.QCart.cart;
import static com.jpshoppingmall.domain.product.entity.QProduct.product;
import static com.jpshoppingmall.domain.product.entity.QProductImage.productImage;

import com.jpshoppingmall.domain.cart.dto.CartInfoDto;
import com.jpshoppingmall.domain.cart.dto.CartProductDto;
import com.jpshoppingmall.domain.cart.entity.Cart;
import com.jpshoppingmall.domain.product.entity.Product;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class CartCustomRepositoryImpl implements CartCustomRepository {

    private final JPAQueryFactory query;

    public List<Cart> getMemberCarts(Long memberId, Set<Long> cartIds) {
        List<Cart> carts = query
            .selectFrom(cart)
//            .leftJoin(cart.member, member)
//            .fetchJoin()
            .where(
                cart.id.in(cartIds),
                cart.member.id.eq(memberId),
                cart.isActive.eq(true)
            )
            .fetch();

        return carts;
    }

    // TODO : N+1 확인
    public List<CartInfoDto> getCartInfoList(Long memberId, List<Long> cartIds) {
        List<Cart> carts = query
            .selectFrom(cart)
            .join(cart.product, product).fetchJoin()
            .where(
                cart.id.in(cartIds),
                cart.member.id.eq(memberId),
                cart.isActive.eq(true)
            )
            .fetch();

        List<CartInfoDto> cartInfoDtoList = new ArrayList<>();
        for (Cart cart1 : carts) {
            Product cartProduct = cart1.getProduct();
            if (cartProduct.getDiscountPercent() == null || cartProduct.getDiscountPercent() == 0) {
                cartInfoDtoList.add(
                    new CartInfoDto(cart1.getId(), cartProduct.getName(),
                        (int) (cart1.getItemCount() * cartProduct.getPrice()), 0));
            } else {
                // 아래와 같이 int casting이 없으면 0으로 계산됨..?
//                log.info("[CartCustomRepositoryImpl] discountedAmount :: {}",
//                    cartProduct.getPrice() * ((100 - cartProduct.getDiscountPercent()) / 100));
                cartInfoDtoList.add(
                    new CartInfoDto(cart1.getId(), cartProduct.getName(),
                        cartProduct.getPrice() * cart1.getItemCount(), Math.round(
                        cartProduct.getPrice() * (cartProduct.getDiscountPercent().floatValue()
                            / 100)) * cart1.getItemCount()));
            }
        }

        return cartInfoDtoList;
    }


    public List<CartProductDto> getActiveMemberCartProducts(Long memberId) {
        return query
            .select(Projections.constructor(CartProductDto.class,
                cart.id.as("cartId"),
                cart.product.id.as("productId"),
                cart.product.name,
                cart.product.price,
                cart.product.discountPercent,
                cart.itemCount,
                productImage.storeFileName
            ))
            .from(cart)
            .leftJoin(cart.product, product)
            .leftJoin(product.productImageList, productImage)
            .where(
                cart.member.id.eq(memberId),
                cart.isActive.eq(true),
                productImage.isTitle.eq(true)
            )
            .orderBy(cart.modifiedDateTime.desc())
            .fetch();
    }

    private JPAQuery<Long> getCount(Long memberId) {
        return query
            .select(cart.count())
            .from(cart)
            .where(
                cart.member.id.eq(memberId)
            );
    }
}
