package com.n26.transaction.model;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TransactionCache {

    private Map<Long, Double> transactionStatsMap;
    private Map<Long, Double> transactionLogMap;

    @PostConstruct
    public void initialize(){
        this.transactionStatsMap = new ConcurrentHashMap<Long, Double>();
        this.transactionLogMap = new ConcurrentHashMap<Long, Double>();
    }

    public void reset(){
        this.transactionStatsMap.clear();
    }

    public Map<Long, Double> getTransactionLogMap() {
        return transactionLogMap;
    }

    public void setTransactionLogMap(Map<Long, Double> transactionLogMap) {
        this.transactionLogMap = transactionLogMap;
    }

    public Map<Long, Double> getTransactionStatsMap() {
        return transactionStatsMap;
    }

    public void setTransactionStatsMap(Map<Long, Double> transactionStatsMap) {
        this.transactionStatsMap = transactionStatsMap;
    }
}
