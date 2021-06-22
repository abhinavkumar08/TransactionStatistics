package com.n26.transaction.config;

import com.n26.transaction.model.TransactionCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class TransactionRemoveBackgroundTask {

    @Autowired
    private TransactionCache transactionCache;
    @Autowired
    private ApplicationProperties applicationProperties;
    static final Logger LOGGER = LoggerFactory.getLogger(TransactionRemoveBackgroundTask.class);

    @EventListener
    public void removeOldTransaction(ContextRefreshedEvent event) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        long evictionTime = applicationProperties.getCacheEvictionTime();
                        //Every e seconds the cache is cleaned up to avoid piling up of cache
                        //removed entry is put into back up log.
                        Thread.sleep(evictionTime);
                    } catch (InterruptedException e) {
                        LOGGER.error("Error Occurred while trying to sleep the background thread to backup transaction " + e.getMessage());
                    }
                    LOGGER.info("Removing older transaction from Cache");
                    Map<Long, Double> transactionCacheMap = transactionCache.getTransactionStatsMap();
                    if (transactionCacheMap != null) {
                        transactionCacheMap.keySet()
                                .removeIf(el -> {
                                    long currTimeInMilliSec = new Date().getTime();
                                    if (currTimeInMilliSec - el > applicationProperties.getTransactionPersistDuration()) {
                                        transactionCache.getTransactionLogMap().put(el, transactionCache.getTransactionStatsMap().get(el));
                                        return true;
                                    }
                                    return false;
                                });
                    }
                }
            }
        });
        thread.start();
    }
}
