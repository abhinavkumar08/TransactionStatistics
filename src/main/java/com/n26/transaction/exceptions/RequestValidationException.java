package com.n26.transaction.exceptions;

public class RequestValidationException extends Exception {

    private static final long serialVersionUID = 1L;

    public RequestValidationException() {
        super();
    }

    public RequestValidationException(String message, Throwable cause, boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public RequestValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestValidationException(String message) {
        super(message);
    }

    public RequestValidationException(Throwable cause) {
        super(cause);
    }


}
