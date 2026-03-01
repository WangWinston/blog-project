package com.blog.common.util;

import java.util.Objects;

/**
 * Null-safe comparison utility methods
 */
public final class CompareUtil {

    private CompareUtil() {
    }

    /**
     * Check if two objects are equal (null-safe)
     */
    public static boolean equals(Object a, Object b) {
        return Objects.equals(a, b);
    }

    /**
     * Check if two objects are not equal (null-safe)
     */
    public static boolean notEquals(Object a, Object b) {
        return !Objects.equals(a, b);
    }

    /**
     * Check if all objects are null
     */
    public static boolean allNull(Object... objects) {
        for (Object obj : objects) {
            if (obj != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if any object is null
     */
    public static boolean anyNull(Object... objects) {
        for (Object obj : objects) {
            if (obj == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if all objects are non-null
     */
    public static boolean allNonNull(Object... objects) {
        for (Object obj : objects) {
            if (obj == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if any object is non-null
     */
    public static boolean anyNonNull(Object... objects) {
        for (Object obj : objects) {
            if (obj != null) {
                return true;
            }
        }
        return false;
    }
}