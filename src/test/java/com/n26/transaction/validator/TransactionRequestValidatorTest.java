package com.n26.transaction.validator;

import com.n26.transaction.exceptions.RequestValidationException;
import com.n26.transaction.model.TransactionRequest;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;

import static com.n26.transaction.util.TransactionUtil.DATE_FORMAT_PATTERN;
import static org.junit.Assert.assertEquals;

public class TransactionRequestValidatorTest {

    @Test
    public void isTransactionValidWithCorrectDataTest() throws RequestValidationException {

        String amount = "123213.1232";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String date = dateFormat.format(new Date());

        TransactionRequest transactionRequest = new TransactionRequest(amount, date);
        boolean validTransaction = TransactionRequestValidator.isTransactionValid(transactionRequest);
        assertEquals(true, validTransaction);
    }

    @Test(expected = RequestValidationException.class)
    public void isTransactionValidWithIncorrectAmount() throws RequestValidationException {

        String amount = "123d213.1232";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String date = dateFormat.format(new Date());

        TransactionRequest transactionRequest = new TransactionRequest(amount, date);
        TransactionRequestValidator.isTransactionValid(transactionRequest);
    }

    @Test(expected = RequestValidationException.class)
    public void isTransactionValidWithFutureDate() throws RequestValidationException {

        String amount = "123213.1232";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date futureDate = Date.from(Instant.now().plusSeconds(1000));
        String date = dateFormat.format(futureDate);
        TransactionRequest transactionRequest = new TransactionRequest(amount, date);
        TransactionRequestValidator.isTransactionValid(transactionRequest);
    }

    @Test(expected = RequestValidationException.class)
    public void isTransactionValidWithIncorrectDate() throws RequestValidationException {

        String amount = "123213.1232";
        String date = "20d2-54-12";
        TransactionRequest transactionRequest = new TransactionRequest(amount, date);
        TransactionRequestValidator.isTransactionValid(transactionRequest);
    }
}
