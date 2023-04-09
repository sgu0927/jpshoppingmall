package com.jpshoppingmall.util;

import java.lang.reflect.Method;
import org.springframework.cache.interceptor.KeyGenerator;

public class PurchaseCountCacheKeyGenerator implements KeyGenerator {

    private static final String PURCHASE_COUNT_KEY_PREFIX = "purchase:count:";

    @Override
    public Object generate(Object target, Method method, Object... params) {
        String productId = params[0].toString();
        return PURCHASE_COUNT_KEY_PREFIX + productId;
    }
}
