package me.alii.utils;

import java.util.concurrent.TimeUnit;

public class TimeUtil {

    public static long extractDuration(String durationStr) {
        return Long.parseLong(durationStr.replaceAll("[^0-9]", ""));
    }

    public static TimeUnit parseTimeUnit(String durationStr) {
        String unit = durationStr.replaceAll("[0-9]", "").trim().toLowerCase();
        return switch (unit) {
            case "ms" -> TimeUnit.MILLISECONDS;
            case "m" -> TimeUnit.MINUTES;
            case "h" -> TimeUnit.HOURS;
            case "d" -> TimeUnit.DAYS;
            default -> TimeUnit.SECONDS;
        };
    }

    public static int parseIntOrDefault(String value, int defaultValue) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
