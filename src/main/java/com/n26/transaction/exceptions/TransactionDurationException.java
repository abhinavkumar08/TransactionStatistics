package com.n26.transaction.exceptions;

public class TransactionDurationException extends Exception {

    private static final long serialVersionUID = 1L;

    public TransactionDurationException() {
        super();
    }

    public TransactionDurationException(String message, Throwable cause, boolean enableSuppression,
                                        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public TransactionDurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionDurationException(String message) {
        super(message);
    }

    public TransactionDurationException(Throwable cause) {
        super(cause);
    }


}
