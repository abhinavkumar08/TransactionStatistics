package com.n26.transaction.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class TransactionExcpetionHandler {

    @ExceptionHandler(RequestValidationException.class)
    public ResponseEntity<?> handleRequestValidationException(RequestValidationException exception, WebRequest request) {

        ErrorResponse response = new ErrorResponse(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity(response, HttpStatus.UNPROCESSABLE_ENTITY);

    }

    @ExceptionHandler(TransactionDurationException.class)
    public ResponseEntity<?> handleTransactionDurationExcpetion(TransactionDurationException exception, WebRequest request) {

        ErrorResponse response = new ErrorResponse(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity(response, HttpStatus.NO_CONTENT);

    }
}
