package com.n26.transaction.controller;

import com.n26.transaction.model.TransactionStatsResponse;
import com.n26.transaction.service.TransactionServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
public class TransactionStatsController {

    @Autowired
    private TransactionServiceImpl transactionServiceImpl;
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionStatsController.class);

    @RequestMapping(method = RequestMethod.GET)
    public TransactionStatsResponse getTransactionStats() {
        LOGGER.info("Transaction request received to get statistics.");
        return transactionServiceImpl.getTransactionStats();
    }
}
