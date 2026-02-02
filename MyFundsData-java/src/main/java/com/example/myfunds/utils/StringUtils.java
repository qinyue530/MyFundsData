package com.example.myfunds.utils;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 字符串工具类
 * 基于 Hutools StrUtil 扩展
 */
public class StringUtils {
    
    /**
     * 空字符串
     */
    public static final String EMPTY = "";
    
    /**
     * 检查字符串是否为空
     * @param str 字符串
     * @return true 为空，false 不为空
     */
    public static boolean isEmpty(String str) {
        return StrUtil.isEmpty(str);
    }
    
    /**
     * 检查字符串是否不为空
     * @param str 字符串
     * @return true 不为空，false 为空
     */
    public static boolean isNotEmpty(String str) {
        return StrUtil.isNotEmpty(str);
    }
    
    /**
     * 检查字符串是否为空白
     * @param str 字符串
     * @return true 为空白，false 不为空白
     */
    public static boolean isBlank(String str) {
        return StrUtil.isBlank(str);
    }
    
    /**
     * 检查字符串是否不为空白
     * @param str 字符串
     * @return true 不为空白，false 为空白
     */
    public static boolean isNotBlank(String str) {
        return StrUtil.isNotBlank(str);
    }
    
    /**
     * 截断字符串
     * @param str 字符串
     * @param length 截断长度
     * @return 截断后的字符串
     */
    public static String truncate(String str, int length) {
        return truncate(str, length, "...");
    }
    
    /**
     * 截断字符串
     * @param str 字符串
     * @param length 截断长度
     * @param suffix 后缀
     * @return 截断后的字符串
     */
    public static String truncate(String str, int length, String suffix) {
        Assert.isTrue(length >= 0, "length must be >= 0");
        if (str == null) {
            return null;
        }
        if (str.length() <= length) {
            return str;
        }
        return str.substring(0, length) + suffix;
    }
    
    /**
     * 首字母大写
     * @param str 字符串
     * @return 首字母大写后的字符串
     */
    public static String capitalize(String str) {
        return StrUtil.upperFirst(str);
    }
    
    /**
     * 首字母小写
     * @param str 字符串
     * @return 首字母小写后的字符串
     */
    public static String uncapitalize(String str) {
        return StrUtil.lowerFirst(str);
    }
    
    /**
     * 字符串转下划线格式
     * @param str 字符串
     * @return 下划线格式字符串
     */
    public static String toUnderlineCase(String str) {
        return StrUtil.toUnderlineCase(str);
    }
    
    /**
     * 字符串转驼峰格式
     * @param str 字符串
     * @return 驼峰格式字符串
     */
    public static String toCamelCase(String str) {
        return StrUtil.toCamelCase(str);
    }
    
    /**
     * 字符串转帕斯卡格式
     * @param str 字符串
     * @return 帕斯卡格式字符串
     */
    public static String toPascalCase(String str) {
        return StrUtil.upperFirst(StrUtil.toCamelCase(str));
    }
    
    /**
     * 移除字符串中的空格
     * @param str 字符串
     * @return 移除空格后的字符串
     */
    public static String removeWhitespace(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.replaceAll("\\s+", "");
    }
    
    /**
     * 格式化字符串
     * @param format 格式字符串
     * @param args 参数
     * @return 格式化后的字符串
     */
    public static String format(String format, Object... args) {
        return StrUtil.format(format, args);
    }
    
    /**
     * 连接字符串
     * @param separator 分隔符
     * @param strs 字符串数组
     * @return 连接后的字符串
     */
    public static String join(String separator, String... strs) {
        return StrUtil.join(separator, strs);
    }
    
    /**
     * 反转字符串
     * @param str 字符串
     * @return 反转后的字符串
     */
    public static String reverse(String str) {
        return StrUtil.reverse(str);
    }
    
    /**
     * 检查字符串是否以指定前缀开头
     * @param str 字符串
     * @param prefixes 前缀数组
     * @return true 以指定前缀开头，false 不以指定前缀开头
     */
    public static boolean startsWithAny(String str, String... prefixes) {
        return StrUtil.startWithAny(str, prefixes);
    }
    
    /**
     * 检查字符串是否以指定后缀结尾
     * @param str 字符串
     * @param suffixes 后缀数组
     * @return true 以指定后缀结尾，false 不以指定后缀结尾
     */
    public static boolean endsWithAny(String str, String... suffixes) {
        return StrUtil.endWithAny(str, suffixes);
    }
    
    /**
     * 重复字符串
     * @param str 字符串
     * @param repeat 重复次数
     * @return 重复后的字符串
     */
    public static String repeat(String str, int repeat) {
        return StrUtil.repeat(str, repeat);
    }
}