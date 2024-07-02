package com.clickcraft.demo.utils;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.Duration;

public class ApplicationTimeFormatter {
    public static String formatApplicationTime(LocalDateTime applicationTime) {
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(applicationTime, currentTime);
        long hours = duration.toHours();

        if (hours == 0) {
            long minutes = duration.toMinutes();
            if (minutes < 1) {
                return "Just now";
            } else if (minutes == 1) {
                return "One minute ago";
            } else {
                return minutes + " minutes ago";
            }
        } else if (hours == 1) {
            return "One hour ago";
        } else if (hours < 24) {
            return hours + " hours ago";
        } else if (hours < 48) {
            return "Yesterday";
        } else {
            return applicationTime.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
        }
    }
}
