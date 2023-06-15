package com.pingpongx.smb.monitor.biz.util;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

    public static String getFormattedTime() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyMMdd");
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HHmmss");
        return date.format(formatterDate) + time.format(formatterTime);
    }

    public static String getFormattedTime2() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss");
        Instant instant = Instant.now();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.of("GMT+08:00"));
        return formatter.format(localDateTime);
    }

    public static void main(String[] args) {
        System.out.println(getFormattedTime2());
    }
}
