package com.fubukigrin.utilities;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ConvertDateTime {
    public static long getEpochSecond(String timestamp) {
        DateTimeFormatter osuFormat = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        Instant parseTime = Instant.from(osuFormat.parse(timestamp));
        return parseTime.getEpochSecond();
    }

    public static long getEpochSecondInstantFormat(String timestamp) {
        DateTimeFormatter osuFormat = DateTimeFormatter.ISO_INSTANT;
        Instant parseTime = Instant.from(osuFormat.parse(timestamp));
        return parseTime.getEpochSecond();
    }

    public static String toShortDateDiscordTimestamp(String timestamp) {
        return String.format("<t:%d:d>", getEpochSecond(timestamp));
    }

    public static String toRelativeDiscordTimestamp(String timestamp) {
        return String.format("<t:%d:R>", getEpochSecond(timestamp));
    }

    // Score "created_at" converter
    public static String toRelativeDiscordTimestampInstantFormat(String timestamp) {
        return String.format("<t:%d:R>", getEpochSecondInstantFormat(timestamp));
    }

    public static String toShortDate(String timestamp) {
        DateTimeFormatter osuFormat = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        LocalDate parseDate = LocalDate.parse(timestamp, osuFormat);
        return parseDate.toString();
    }

    public static String toLongDateTime(String timestamp) {
        DateTimeFormatter osuFormat = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        DateTimeFormatter longDateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Instant parseTime = Instant.from(osuFormat.parse(timestamp));
        String parseDate = longDateTimeFormat.format(parseTime);
        return parseDate;
    }

    public static String toRelativeTime(String timestamp) {
        DateTimeFormatter osuFormat = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        Instant now = Instant.now();
        Instant parseTime = Instant.from(osuFormat.parse(timestamp));

        long timeInBetween = now.getEpochSecond() - parseTime.getEpochSecond();

        String relative = new String();

        if (timeInBetween < 60) {
            // Seconds
            relative = (timeInBetween > 1) ? timeInBetween + " seconds ago" : "1 second ago";
        } 
        else if (timeInBetween < 3600) {
            // Minutes
            timeInBetween /= 60;
            relative = (timeInBetween > 1) ? timeInBetween + " minutes ago" : "1 minute ago";
        }
        else if (timeInBetween < 86400) {
            // Hours
            timeInBetween /= 3600;
            relative = (timeInBetween > 1) ? timeInBetween + " hours ago" : "1 hour ago";
        }
        else if (timeInBetween < 5184000) {
            // Days
            timeInBetween /= 86400;
            relative = (timeInBetween > 1) ? timeInBetween + " days ago" : "1 day ago";
        }
        else if (timeInBetween < 31557600) {
            // Months
            timeInBetween /= 2592000;
            relative = timeInBetween + " months ago";
        }
        else {
            // Years (to accomendate leap years, I treat it as 365.25 days)
            timeInBetween /= 31557600;
            relative = (timeInBetween > 1) ? timeInBetween + " days ago" : "1 year ago";
        }

        return relative;
    }
}
