package com.cicro.vhr.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TimeUtils {

    /*
     * @methodName:
     * @description 比较两个时间段相差的几年
     * @param: beginContract 起始时间
     *          endContract  结束时间
     * @return: double 相差年数
     * @createdAt 17:28 2020/8/3 0003
     * @version 1.0.0
     **/
    public static double until(Date endContract, Date beginContract) {
        LocalDate startTime = date2LocalDate(beginContract);
        LocalDate endTime = date2LocalDate(endContract);
        Long until = startTime.until(endTime, ChronoUnit.DAYS) / 365;
        System.out.println(until);
        return until.doubleValue();
    }

    public static Date format(Date date) {
        LocalDate localDate = date2LocalDate(date);
        String format = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Instant instant =
            LocalDate.parse(format, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();

        return Date.from(instant);
    }

    public static LocalDate date2LocalDate(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalDate();

    }

    public static void main(String[] args) {
        Date date=new Date(2019,10,11);
        Date format = format(date);
        System.out.println(format);
    }
}
