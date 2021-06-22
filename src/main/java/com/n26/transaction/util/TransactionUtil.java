package com.n26.transaction.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TransactionUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionUtil.class);
    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public static Date parseDateFromString(String dateStr) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            LOGGER.error("Error occurred while parsing date format " + e.getMessage());
            throw e;
        }
        return date;
    }

    public static String formatUptoTwoDecimalPlace(double number) {
        double roundoff = Math.round(number * 100.00) / 100.0;
        return String.format("%.2f", roundoff);
    }
}
