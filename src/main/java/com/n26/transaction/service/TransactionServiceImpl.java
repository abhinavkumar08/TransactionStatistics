package com.n26.transaction.service;

import com.n26.transaction.config.ApplicationProperties;
import com.n26.transaction.controller.TransactionController;
import com.n26.transaction.model.TransactionCache;
import com.n26.transaction.model.TransactionRequest;
import com.n26.transaction.model.TransactionStatsResponse;
import com.n26.transaction.util.TransactionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionCache transactionCache;

    @Autowired
    private ApplicationProperties applicationProperties;

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);

    public TransactionServiceImpl(TransactionCache transactionCache, ApplicationProperties applicationProperties) {
        this.transactionCache = transactionCache;
        this.applicationProperties = applicationProperties;
    }

    public void saveTransaction(TransactionRequest transaction) throws ParseException {
        Double amount = Double.parseDouble(transaction.getAmount());
        Date date = TransactionUtil.parseDateFromString(transaction.getTimestamp());
        LOGGER.info("Saving transaction to cache.");
        long transactionTimeInMilliSec = date.getTime();
            Map<Long, Double> hashmap = transactionCache.getTransactionStatsMap();
            if(!hashmap.containsKey(transactionTimeInMilliSec)){
                hashmap.put(transactionTimeInMilliSec, amount);
            }else{
                long key = transactionTimeInMilliSec+1;
                while (hashmap.containsKey(key)){
                    key++;
                }
                hashmap.put(key, amount);
            }
            LOGGER.debug("Transaction entry saved.");
    }

    public TransactionStatsResponse getTransactionStats() {

        LOGGER.info("Fetching transaction entry statistics.");
        long currTimeInSec = new Date().getTime();
        LOGGER.debug("Filtering out transactions which are old.");
        Collection<Double> transactionValues = transactionCache.getTransactionStatsMap()
                .entrySet().stream()
                .filter(map -> currTimeInSec -map.getKey()<= applicationProperties.getTransactionPersistDuration())
                .map(map -> map.getValue())
                .collect(Collectors.toList());

        String sum = TransactionUtil.formatUptoTwoDecimalPlace(transactionValues
                .stream()
                .mapToDouble(d -> d)
                .sum());

        String average = TransactionUtil.formatUptoTwoDecimalPlace(transactionValues
                .stream()
                .mapToDouble(d -> d)
                .average()
                .orElse(0.00d));

        String max = TransactionUtil.formatUptoTwoDecimalPlace(transactionValues
                .stream()
                .mapToDouble(d -> d)
                .max()
                .orElse(0.00d));

        String min = TransactionUtil.formatUptoTwoDecimalPlace(transactionValues
                .stream()
                .mapToDouble(d -> d)
                .min()
                .orElse(0.00d));

        long count = transactionValues.size();
        LOGGER.info("Transaction stats fetched, returning response.");
        TransactionStatsResponse response = new TransactionStatsResponse(sum, average, max, min, count);
        return response;
    }

    @Override
    public void deleteAllTransaction() {
        transactionCache.reset();
    }
}
