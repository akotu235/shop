package io.github.akotu235.shop.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormatUtils {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public static String formatDate(LocalDateTime date) {
        if (date != null) {
            return date.format(FORMATTER);
        } else {
            return null;
        }
    }

    public static String formatPrice(double price) {
        return String.format("%.2f", price).replace('.', ',');
    }

    public static double roundPriceToTwoDecimalPlaces(double value) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
