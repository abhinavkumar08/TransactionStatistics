package com.n26.transaction.util;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TransactionUtilTest {

    @Test(expected = ParseException.class)
    public void parseDateFromStringWTest() throws ParseException {
        String dateStr = new Date().toString();
        Date date = TransactionUtil.parseDateFromString(dateStr);
        Assert.assertEquals(Date.from(Instant.now().truncatedTo(ChronoUnit.DAYS)), date);
    }

    @Test
    public void formatUptoTwoDecimalPlaceTest() {
        double number = 32432.32552;
        String actual = TransactionUtil.formatUptoTwoDecimalPlace(number);
        Assert.assertEquals("32432.33", actual);
    }
}
