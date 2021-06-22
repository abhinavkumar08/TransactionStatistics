package com.n26.transaction.service;

import com.n26.transaction.config.ApplicationProperties;
import com.n26.transaction.model.TransactionCache;
import com.n26.transaction.model.TransactionRequest;
import com.n26.transaction.model.TransactionStatsResponse;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import static com.n26.transaction.util.TransactionUtil.DATE_FORMAT_PATTERN;

public class TransactionServiceImplTest {

    private TransactionService transactionService;

    @MockBean
    private ApplicationProperties applicationProperties;
    @MockBean
    private TransactionCache transactionCache;

    @Before
    public void init() {
        transactionCache = Mockito.mock(TransactionCache.class);
        applicationProperties = Mockito.mock(ApplicationProperties.class);
        transactionService = new TransactionServiceImpl(transactionCache, applicationProperties);
        Mockito.when(applicationProperties.getTransactionPersistDuration()).thenReturn(60000l);
    }

    @Test
    public void saveTransactionTest() throws ParseException {
        String amount = "123213.1232";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String date = dateFormat.format(new Date());
        long dateInMilliSec = new Date().getTime();
        TransactionRequest transactionRequest = new TransactionRequest(amount, date);
        Map<Long, Double> transactionMap = getTransactionMap(dateInMilliSec);
        Mockito.when(transactionCache.getTransactionStatsMap()).thenReturn(transactionMap);
        transactionService.saveTransaction(transactionRequest);
        Mockito.verify(transactionCache, Mockito.times(1)).getTransactionStatsMap();
    }

    @Test
    public void getTransactionStatsTest() {
        String amount = "123213.1232";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String date = dateFormat.format(new Date());
        long dateInMilliSec = new Date().getTime();
        Map<Long, Double> transactionMap = getTransactionMap(dateInMilliSec);
        Mockito.when(transactionCache.getTransactionStatsMap()).thenReturn(transactionMap);
        TransactionStatsResponse response = transactionService.getTransactionStats();
        TransactionStatsResponse expected = new TransactionStatsResponse("1781.90", "296.98", "554.12", "123.12", 6l);
        Assertions.assertThat(response).isEqualToComparingFieldByField(expected);
    }

    @Test
    public void deleteAllTransaction() {

        transactionService.deleteAllTransaction();
        Mockito.verify(transactionCache, Mockito.times(1)).reset();
    }

    private static Map<Long, Double> getTransactionMap(long dateInMilliSec) {
        Map<Long, Double> hashmap = new ConcurrentHashMap<Long, Double>();
        hashmap.put(dateInMilliSec - 2, 129.12);
        hashmap.put(dateInMilliSec - 1, 423.12);
        hashmap.put(dateInMilliSec, 123.12);
        hashmap.put(dateInMilliSec + 1, 321.12);
        hashmap.put(dateInMilliSec + 2, 554.12);
        hashmap.put(dateInMilliSec - 1000, 231.3);
        return hashmap;
    }
}
