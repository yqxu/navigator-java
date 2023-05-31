package com.pingpongx.smb.monitor.biz.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

    public static String getFormattedTime() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HHmmss");
        return date.format(formatterDate) + time.format(formatterTime);
    }

    public static String getFormattedTime2() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yy-MM-dd");
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH-mm-ss");
        return date.format(formatterDate) + "-" + time.format(formatterTime);
    }

    public static void main(String[] args) {
        System.out.println(getFormattedTime2());
    }
}
