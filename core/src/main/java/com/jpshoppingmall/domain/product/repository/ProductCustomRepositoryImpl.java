package com.jpshoppingmall.domain.product.repository;

import static com.jpshoppingmall.domain.category.entity.QCategory.category;
import static com.jpshoppingmall.domain.product.entity.QProduct.product;
import static com.jpshoppingmall.domain.product.entity.QProductImage.productImage;

import com.jpshoppingmall.domain.product.dto.ProductDetailDto;
import com.jpshoppingmall.domain.product.entity.Product;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

    private final JPAQueryFactory query;

    @Override
    public List<Product> getTopNineProductsWithImages() {
        return query
            .selectFrom(product)
            .join(product.productImageList, productImage)
            .distinct()
            .limit(9L)
            .fetch();
    }

    @Override
    public Product getProductWithImages(Long productId) {
        return query
            .selectFrom(product)
            .join(product.productImageList, productImage).fetchJoin()
            .where(product.id.eq(productId))
            .fetchOne();
    }

    @Override
    public Page<ProductDetailDto> getCategoryProducts(List<Long> categoryIds, Pageable pageable) {
        List<Product> contents = query
            .selectFrom(product)
            .leftJoin(product.category, category)
            .where(
                product.category.id.in(categoryIds)
                // product.isConfirm.eq(true)
            )
            .orderBy(productSort(pageable))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = getCount(categoryIds);

        return PageableExecutionUtils.getPage(contents.stream().map(Product::toDetailDto).toList(),
            pageable, countQuery::fetchOne);
    }

    private JPAQuery<Long> getCount(List<Long> categoryIds) {
        return query
            .select(product.count())
            .from(product)
            .leftJoin(product.category, category)
            .where(
                product.category.id.in(categoryIds)
            );
    }

    private OrderSpecifier<?> productSort(Pageable page) {
        //서비스에서 보내준 Pageable 객체에 정렬조건 null 값 체크
        if (!page.getSort().isEmpty()) {
            //정렬값이 들어 있으면 for 사용하여 값을 가져온다
            for (Sort.Order order : page.getSort()) {
                // 서비스에서 넣어준 DESC or ASC 를 가져온다.
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                // 서비스에서 넣어준 정렬 조건을 스위치 케이스 문을 활용하여 셋팅하여 준다.
                switch (order.getProperty()) {
                    case "createdDateTime" -> {
                        return new OrderSpecifier<>(direction, product.createdDateTime);
                    }
                    case "price" -> {
                        return new OrderSpecifier<>(direction, product.price);
                    }
                    case "rating" -> {
                        return new OrderSpecifier<>(direction, product.rating);
                    }
                    case "discountPercent" -> {
                        return new OrderSpecifier<>(direction, product.discountPercent);
                    }
                }
            }
        }
        return null;
    }
}
