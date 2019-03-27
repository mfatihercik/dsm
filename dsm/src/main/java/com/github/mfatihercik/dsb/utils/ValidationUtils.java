package com.github.mfatihercik.dsb.utils;

import com.github.mfatihercik.dsb.DCMValidationException;

public class ValidationUtils {
    public static void assertTrue(boolean condition, String message) {
        if (condition)
            throw new DCMValidationException(message);
    }
}
