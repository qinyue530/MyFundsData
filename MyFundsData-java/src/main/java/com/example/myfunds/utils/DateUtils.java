package com.example.myfunds.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.format.FastDateFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期时间工具类
 * 基于 Hutools DateUtil 扩展
 */
public class DateUtils {
    
    /**
     * 常用日期格式
     */
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String TIME_PATTERN = "HH:mm:ss";
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATETIME_PATTERN_WITH_MILLIS = "yyyy-MM-dd HH:mm:ss.SSS";
    
    /**
     * 格式化LocalDateTime为字符串
     * @param dateTime LocalDateTime对象
     * @param pattern 日期格式
     * @return 格式化后的字符串
     */
    public static String formatLocalDateTime(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }
    
    /**
     * 格式化LocalDateTime为默认格式字符串(yyyy-MM-dd HH:mm:ss)
     * @param dateTime LocalDateTime对象
     * @return 格式化后的字符串
     */
    public static String formatLocalDateTime(LocalDateTime dateTime) {
        return formatLocalDateTime(dateTime, DATETIME_PATTERN);
    }
    
    /**
     * 格式化LocalDate为字符串
     * @param date LocalDate对象
     * @param pattern 日期格式
     * @return 格式化后的字符串
     */
    public static String formatLocalDate(LocalDate date, String pattern) {
        if (date == null) {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }
    
    /**
     * 格式化LocalDate为默认格式字符串(yyyy-MM-dd)
     * @param date LocalDate对象
     * @return 格式化后的字符串
     */
    public static String formatLocalDate(LocalDate date) {
        return formatLocalDate(date, DATE_PATTERN);
    }
    
    /**
     * 将字符串解析为LocalDateTime
     * @param dateStr 日期字符串
     * @param pattern 日期格式
     * @return LocalDateTime对象
     */
    public static LocalDateTime parseLocalDateTime(String dateStr, String pattern) {
        if (dateStr == null) {
            return null;
        }
        return LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
    }
    
    /**
     * 将字符串解析为LocalDateTime，使用默认格式(yyyy-MM-dd HH:mm:ss)
     * @param dateStr 日期字符串
     * @return LocalDateTime对象
     */
    public static LocalDateTime parseLocalDateTime(String dateStr) {
        return parseLocalDateTime(dateStr, DATETIME_PATTERN);
    }
    
    /**
     * 获取当前日期时间
     * @return 当前日期时间字符串(yyyy-MM-dd HH:mm:ss)
     */
    public static String getCurrentDateTimeStr() {
        return DateUtil.now();
    }
    
    /**
     * 获取当前日期
     * @return 当前日期字符串(yyyy-MM-dd)
     */
    public static String getCurrentDateStr() {
        return DateUtil.today();
    }
    
    /**
     * 计算两个日期之间的天数差
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 天数差
     */
    public static long betweenDays(Date startDate, Date endDate) {
        return DateUtil.between(startDate, endDate, DateUnit.DAY);
    }
    
    /**
     * 格式化日期为指定格式
     * @param date 日期对象
     * @param pattern 日期格式
     * @return 格式化后的字符串
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        return FastDateFormat.getInstance(pattern).format(date);
    }
    
    /**
     * 格式化日期为默认格式(yyyy-MM-dd HH:mm:ss)
     * @param date 日期对象
     * @return 格式化后的字符串
     */
    public static String format(Date date) {
        return format(date, DATETIME_PATTERN);
    }
    
    /**
     * 解析字符串为日期对象
     * @param dateStr 日期字符串
     * @param pattern 日期格式
     * @return 日期对象
     */
    public static Date parse(String dateStr, String pattern) {
        if (dateStr == null) {
            return null;
        }
        return DateUtil.parse(dateStr, pattern);
    }
    
    /**
     * 解析字符串为日期对象，使用默认格式(yyyy-MM-dd HH:mm:ss)
     * @param dateStr 日期字符串
     * @return 日期对象
     */
    public static Date parse(String dateStr) {
        return parse(dateStr, DATETIME_PATTERN);
    }
    
    /**
     * 获取指定日期的开始时间
     * @param date 日期对象
     * @return 当天开始时间(00:00:00)
     */
    public static Date getStartOfDay(Date date) {
        return DateUtil.beginOfDay(date);
    }
    
    /**
     * 获取指定日期的结束时间
     * @param date 日期对象
     * @return 当天结束时间(23:59:59)
     */
    public static Date getEndOfDay(Date date) {
        return DateUtil.endOfDay(date);
    }
}