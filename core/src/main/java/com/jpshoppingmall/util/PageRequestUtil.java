package com.jpshoppingmall.util;

import com.jpshoppingmall.type.EnumMaster.PagingType;
import com.jpshoppingmall.type.EnumMaster.SortType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

@ToString
public final class PageRequestUtil {

    private static final String CREATED_DATE_TIME = "createdDateTime";
    private static final String ORDERED_DATE_TIME = "orderedDateTime";
    private static final String PRICE = "price";
    private static final String RATING = "rating";
    private static final String DISCOUNT_PERCENT = "discountPercent";
    private static final int ORDER_SIZE = 10;
    @Getter
    private int page = 0;

    @Setter
    @Getter
    private int size = 2;
    @Getter
    @Setter
    private SortType sortType;

    public void setPage(int page) {
        this.page = Math.max(page, 0);
    }

    public org.springframework.data.domain.PageRequest of(PagingType pagingType) {
//        return org.springframework.data.domain.PageRequest
//            .of(page, size, Sort.by(Order.asc(PRICE)));

        if (pagingType.equals(PagingType.PRODUCT)) {
            sortType = sortType == null ? SortType.RATING_DESC : sortType;
        } else if (pagingType.equals(PagingType.CART)) {
            sortType = sortType == null ? SortType.NEWEST : sortType;
        } else if (pagingType.equals(PagingType.ORDER)) {
            sortType = sortType == null ? SortType.ORDERED_DESC : sortType;
            // size = ORDER_SIZE;
        } else {
            sortType = sortType == null ? SortType.NEWEST : sortType;
        }
//
//        // 신규 PagingType이 추가되면 추가검증 필요
//        validate(pagingType);
//
        return switch (sortType) {
            case RATING_DESC -> org.springframework.data.domain.PageRequest
                .of(page, size, Sort.by(Order.desc(RATING)));
            case PRICE_ASC -> org.springframework.data.domain.PageRequest
                .of(page, size, Sort.by(Order.asc(PRICE)));
            case PRICE_DESC -> org.springframework.data.domain.PageRequest
                .of(page, size, Sort.by(Order.desc(PRICE)));
            case ORDERED_DESC -> org.springframework.data.domain.PageRequest
                .of(page, size, Sort.by(Order.desc(ORDERED_DATE_TIME)));
            case DISCOUNT_PERCENT_DESC -> org.springframework.data.domain.PageRequest
                .of(page, size, Sort.by(Order.desc(DISCOUNT_PERCENT)));
            default -> // NEWEST
                org.springframework.data.domain.PageRequest
                    .of(page, size, Sort.by(Order.desc(CREATED_DATE_TIME)));
        };
    }
}
