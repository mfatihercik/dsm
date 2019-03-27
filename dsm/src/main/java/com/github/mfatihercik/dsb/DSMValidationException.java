package com.github.mfatihercik.dsb;

public class DSMValidationException extends RuntimeException {

    public DSMValidationException() {
    }

    public DSMValidationException(String message) {
        super(message);
    }

    public DSMValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DSMValidationException(Throwable cause) {
        super(cause);
    }

    public DSMValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
