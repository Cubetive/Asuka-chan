package com.fubukigrin.utilities;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ConvertDateTime {
    public static long getEpochSecond(String timestamp) {
        DateTimeFormatter f = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        Instant parseTime = Instant.from(f.parse(timestamp));
        return parseTime.getEpochSecond();
    }

    public static String toShortDateDiscordTimestamp(String timestamp) {
        return String.format("<t:%d:d>", getEpochSecond(timestamp));
    }

    public static String toRelativeDiscordTimestamp(String timestamp) {
        return String.format("<t:%d:R>", getEpochSecond(timestamp));
    }

    public static String toShortDate(String timestamp) {
        DateTimeFormatter f = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        LocalDate parseDate = LocalDate.parse(timestamp, f);
        return parseDate.toString();
    }
}
