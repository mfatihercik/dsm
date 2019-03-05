package com.github.mfatihercik.dsb;

public class DCMValidationException extends RuntimeException {

    public DCMValidationException() {
    }

    public DCMValidationException(String message) {
        super(message);
    }

    public DCMValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DCMValidationException(Throwable cause) {
        super(cause);
    }

    public DCMValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
