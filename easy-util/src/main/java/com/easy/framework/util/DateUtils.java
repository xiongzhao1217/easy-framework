package com.easy.framework.util;

import cn.hutool.core.date.DateException;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * 日期工具类
 *
 * <p>
 * 对 hutool DateUtil 的补充<br>
 * 添加自定义的常用日期处理方法
 * </p>
 *
 * @see DateUtil
 * @author xiongzhao
 */
public class DateUtils extends DateUtil {

    /**
     * 私有构造，禁止 new 对象
     */
    private DateUtils () {}

    /**
     * 格式化日期部分（不包括时间）
     *
     * <p>
     * 格式 yyyy-MM-dd <br>
     * 如果传入 {@code milliseconds} 为 null，返回null；否则返回格式化后的日期
     * </p>
     *
     * @param milliseconds 被格式化的时间毫秒
     * @return 格式化后的字符串，yyyy-MM-dd
     */
    public static String formatDate(Long milliseconds) {
        if (null == milliseconds) {
            return null;
        }
        return formatDate(new Date(milliseconds));
    }

    /**
     * 格式化日期
     *
     * <p>
     * 格式 yyyy-MM-dd HH:mm:ss <br>
     * 如果传入 {@code milliseconds} 为 null，返回null；否则返回格式化后的日期
     * </p>
     *
     * @param milliseconds 被格式化的时间毫秒
     * @return 格式化后的字符串，yyyy-MM-dd HH:mm:ss
     */
    public static String formatDateTime(Long milliseconds) {
        if (null == milliseconds) {
            return null;
        }
        return formatDateTime(new Date(milliseconds));
    }

    /**
     * 格式化日期
     *
     * <p>
     * HH:mm:ss <br>
     * 如果传入 {@code milliseconds} 为 null，返回null；否则返回格式化后的日期
     * </p>
     *
     * @param milliseconds 被格式化的时间毫秒
     * @return 格式化后的字符串，HH:mm:ss
     */
    public static String formatTime(Long milliseconds) {
        if (null == milliseconds) {
            return null;
        }
        return formatTime(new Date(milliseconds));
    }

    /**
     * 格式化为中文日期格式
     *
     * <p>
     * 如果{@code isUppercase}为 false，则返回类似：2018年10月24日<br>
     * 如果{@code isUppercase}为 true，则返回类似：二〇一八年十月二十四日<br>
     * </p>
     *
     * @param milliseconds 被格式化的时间毫秒
     * @return 格式化后的字符串，2018年10月24日 或 二〇一八年十月二十四日
     */
    public static String formatChineseDate(Long milliseconds, boolean isUppercase) {
        if (null == milliseconds) {
            return null;
        }
        return DateUtils.formatChineseDate(new Date(milliseconds), isUppercase);
    }

    /**
     * 日期格式转换，转换失败抛出指定message信息的异常
     * @param dateString
     * @param message
     * @return
     */
    public static DateTime parseDate(CharSequence dateString, String message) {
        try {
            return parseDate(dateString);
        } catch (Exception e) {
            throw new DateException(message);
        }
    }
}
