package com.n26.transaction.validator;

import com.n26.transaction.config.TransactionRemoveBackgroundTask;
import com.n26.transaction.exceptions.RequestValidationException;
import com.n26.transaction.model.TransactionRequest;
import com.n26.transaction.util.TransactionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Date;

public final class TransactionRequestValidator {

    static final Logger LOGGER = LoggerFactory.getLogger(TransactionRemoveBackgroundTask.class);

    private TransactionRequestValidator() {
    }

    public static boolean isTransactionValid(TransactionRequest transactionRequest) throws RequestValidationException {

        try {
            LOGGER.info("Validating transaction request.");
            Date date = TransactionUtil.parseDateFromString(transactionRequest.getTimestamp());
            double amount = Double.parseDouble(transactionRequest.getAmount());
            boolean isFutureDate = new Date().getTime() - date.getTime() < 0 ? true : false;
            if (isFutureDate) {
                LOGGER.error("Transaction date is a future date, invalid request ");
                throw new RequestValidationException("Transaction date is a future date, invalid request " + date);
            }
        } catch (ParseException e) {
            LOGGER.error("Unable to parse the date, Invalid request " + transactionRequest.getTimestamp());
            throw new RequestValidationException(e);
        } catch (NumberFormatException e) {
            LOGGER.error("Unable to parse the transaction amount, Invalid request " + transactionRequest.getAmount());
            throw new RequestValidationException(e);
        }
        return true;
    }
}

