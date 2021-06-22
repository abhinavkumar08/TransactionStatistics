package com.n26.transaction.controller;

import com.n26.transaction.config.ApplicationProperties;
import com.n26.transaction.exceptions.RequestValidationException;
import com.n26.transaction.exceptions.TransactionDurationException;
import com.n26.transaction.model.TransactionRequest;
import com.n26.transaction.service.TransactionService;
import com.n26.transaction.util.TransactionUtil;
import com.n26.transaction.validator.TransactionRequestValidator;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private TransactionService transactionService;

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);


    /*
    After getting the transaction request, check the transaction time.
    If the transaction happened before 60 seconds(configurable) from the current time, return 204 AND dont save the transaction.
    If the transaction happened in the 60 seconds validate it and save it into the cache.
     */
    @RequestMapping(method = RequestMethod.POST, consumes = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public void saveTransaction(@RequestBody TransactionRequest transaction) throws RequestValidationException, TransactionDurationException {

        Date date = null;
        try {
            LOGGER.info("Transaction request received.");
            TransactionRequestValidator.isTransactionValid(transaction);
            date = TransactionUtil.parseDateFromString(transaction.getTimestamp());
            long transactionTimeInMilliSec = date.getTime();
            long currTimeInMilliSec = Instant.now().getMillis();
            long diffInTimeInMilliSec = currTimeInMilliSec - transactionTimeInMilliSec;
            LOGGER.info("Transaction was recorded " + diffInTimeInMilliSec / 1000 + " seconds ago.");
            long duration = applicationProperties.getTransactionPersistDuration();
            if (diffInTimeInMilliSec > duration) {
                LOGGER.debug("Transaction is older than " + duration / 1000 + " seconds, Aborting request.");
                throw new TransactionDurationException("Transaction is older than " + duration / 1000 + " seconds, Aborting request.");
            }
            LOGGER.debug("Saving transaction entry to cache.");
            transactionService.saveTransaction(transaction);
        } catch (ParseException e) {
            throw new RequestValidationException(e);
        } catch (RequestValidationException e) {
            LOGGER.error("Request is not valid, Aborting...");
            throw e;
        }

    }

    /*
    * Delete all the transaction entries from the cache and return 204.
    * */
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTransactions() {
        LOGGER.info("Delete Transactions request received.");
        transactionService.deleteAllTransaction();
    }
}
