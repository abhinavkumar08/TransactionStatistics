package com.n26.transaction.service;

import com.n26.transaction.model.TransactionRequest;
import com.n26.transaction.model.TransactionStatsResponse;

import java.text.ParseException;

public interface TransactionService {

    public void saveTransaction(TransactionRequest transaction) throws ParseException;

    public TransactionStatsResponse getTransactionStats();

    public void deleteAllTransaction();
}
