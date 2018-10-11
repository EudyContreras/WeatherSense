package com.eudycontreras.weathersense.utilities.miscellaneous;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;

/**
 * Created by Eudy on 10/7/2016.
 */

public class NumberFormatter {

    public static String createCurrencyValue(String string, String currency) {
        NumberFormat format = NumberFormat.getCurrencyInstance();

        format.setMaximumFractionDigits(2);

        format.setCurrency(Currency.getInstance(currency));

        DecimalFormat decimalFormat = new DecimalFormat("#.00");

        Double newValue = Double.parseDouble(string);

        decimalFormat.format(newValue);

        return format.format(newValue);
    }

    public static String formatDecimals(String string, String currency) {

        DecimalFormat decimalFormat = new DecimalFormat("#.00");

        Double newValue = Double.parseDouble(string);

        return  decimalFormat.format(newValue);
    }

    public static String createCurrencyValue(Double doubleValue, String currency) {
        String string = String.valueOf(doubleValue);

        NumberFormat format = NumberFormat.getCurrencyInstance();

        format.setMaximumFractionDigits(2);

        format.setCurrency(Currency.getInstance(currency));

        DecimalFormat decimalFormat = new DecimalFormat("#.00");

        Double newValue = Double.parseDouble(string);

        decimalFormat.format(newValue);

        return format.format(newValue);
    }

    public static BigDecimal parse(final String amount, final Locale locale) throws ParseException {
        final NumberFormat format = NumberFormat.getNumberInstance(locale);

        if (format instanceof DecimalFormat) {
            ((DecimalFormat) format).setParseBigDecimal(true);
        }

        return (BigDecimal) format.parse(amount.replaceAll("[^\\d.,]", ""));
    }
}
