package com.github.mfatihercik.dsb.utils;

import com.github.mfatihercik.dsb.DSMValidationException;

public class ValidationUtils {
    public static void assertTrue(boolean condition, String message) {
        if (condition)
            throw new DSMValidationException(message);
    }
}
