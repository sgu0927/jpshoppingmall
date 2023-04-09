package com.jpshoppingmall.util;

public final class RedisUtil {

    private RedisUtil() {
    }

    private static final String PURCHASE_COUNT_KEY_PREFIX = "purchase:count:";

    private static final String PURCHASE_COUNT_LOCK_KEY_POSTFIX = ":lock";

    public static String createPurchaseCountKey(long productId) {
        return PURCHASE_COUNT_KEY_PREFIX + productId;
    }

    public static String createPurchaseCountLockKey(long productId) {
        return PURCHASE_COUNT_KEY_PREFIX + productId + PURCHASE_COUNT_LOCK_KEY_POSTFIX;
    }
}
