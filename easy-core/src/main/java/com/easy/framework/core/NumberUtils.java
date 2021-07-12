package com.easy.framework.util;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.NumberUtil;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * 数字工具类
 *
 * <p>
 * 对 {@link cn.hutool.core.util.NumberUtil} 的补充 <br>
 * 包含数学计算功能和数字校验功能 <br>
 * 补充正负数、正负整数、非正整数、非负整数等的正则校验<br>
 * </p>
 *
 * @author xiongzhao
 */
public class NumberUtils extends NumberUtil {

    /**
     * 私有构造，禁止 new 对象
     */
    private NumberUtils() {}

    /**
     * 正数
     */
    public final static Pattern POSITIVE_NUMBER = Pattern.compile("^(\\+)?(([1-9]\\d*(.\\d+)?)|(0.\\d+))$");

    /**
     * 负数
     */
    public final static Pattern NEGATIVE_NUMBER = Pattern.compile("-(([1-9]\\d*(.\\d+)?)|(0.\\d+))$");

    /**
     * 整数(不含零)
     */
    public final static Pattern INT_NUMBER_EXCLUDE_ZERO = Pattern.compile("^(\\-|\\+)?[1-9][0-9]*$");

    /**
     * 正整数
     */
    public final static Pattern POSITIVE_INT_NUMBER = Pattern.compile("(^(\\+)?[1-9]\\d*)$");

    /**
     * 非负整数(大于等于0的整数)
     */
    public final static Pattern INT_NUMBER_GTE_ZERO = Pattern.compile("(^(\\+)?[1-9]\\d*)|0$");

    /**
     * 负整数
     */
    public final static Pattern NEGATIVE_INT_NUMBER = Pattern.compile("-[1-9]\\d*$");

    /**
     * 非正整数(小于等于0的整数)
     */
    public final static Pattern INT_NUMBER_LTE_ZERO = Pattern.compile("(-[1-9]\\d*)|0$");

    /**
     * 最多保留两位小数的正数
     */
    public final static Pattern FLOAT_SCALE_TWO_GTE_ZERO = Pattern.compile("^(([1-9]{1}\\d*)|(0{1}))(\\.\\d{1,2})?$");

    /**
     * 是否正数 (包含正整数和正浮点数)
     *
     * <ul>
     *     <li>{@code isPositiveNumber("123")} 返回 true </li>
     *     <li>{@code isPositiveNumber("1.23")} 返回 true </li>
     *     <li>{@code isPositiveNumber("+1.23")} 返回 true </li>
     *     <li>{@code isPositiveNumber("0.23")} 返回 true </li>
     *     <li>{@code isPositiveNumber("0")} 返回 false </li>
     *     <li>{@code isPositiveNumber("-123")} 返回 false </li>
     *     <li>{@code isPositiveNumber("-1.23")} 返回 false </li>
     *     <li>{@code isPositiveNumber("abc")} 返回 false </li>
     * </ul>
     *
     * @param value 需要校验的字符
     * @return 正数返回 true， 否则返回 false
     */
    public static boolean isPositive (CharSequence value) {
        return Validator.isMactchRegex(POSITIVE_NUMBER, value);
    }

    /**
     * 是否负数 (包含负整数和负浮点数)
     *
     * <ul>
     *     <li>{@code isPositiveNumber("-123")} 返回 true </li>
     *     <li>{@code isPositiveNumber("-1.23")} 返回 true </li>
     *     <li>{@code isPositiveNumber("-0.23")} 返回 true </li>
     *     <li>{@code isPositiveNumber("1.23")} 返回 false </li>
     *     <li>{@code isPositiveNumber("0")} 返回 false </li>
     *     <li>{@code isPositiveNumber("abc")} 返回 false </li>
     * </ul>
     *
     * @param value 需要校验的字符
     * @return 负数返回 true， 否则返回 false
     */
    public static boolean isNegative (CharSequence value) {
        return Validator.isMactchRegex(NEGATIVE_NUMBER, value);
    }

    /**
     * 是否整数(不包括零)
     *
     * <p>
     * 不限制长度，覆盖自然界所有整数(不包括零)
     * 判断是否{@link Integer} 请参考 {@link NumberUtils#isInteger(String)}
     * 判断是否{@link Long} 请参考 {@link NumberUtils#isLong(String)}
     * </p>
     *
     * <ul>
     *     <li>{@code isIntegerExcludeZero("123")} 返回 true </li>
     *     <li>{@code isIntegerExcludeZero("123")} 返回 true </li>
     *     <li>{@code isIntegerExcludeZero("-123")} 返回 true </li>
     *     <li>{@code isIntegerExcludeZero("+123")} 返回 true </li>
     *     <li>{@code isIntegerExcludeZero("0")} 返回 false </li>
     *     <li>{@code isIntegerExcludeZero("abc")} 返回 false </li>
     * </ul>
     *
     * @param value 需要校验的字符
     * @return 整数且不为零返回 true， 否则返回 false
     */
    public static boolean isIntNotZero (CharSequence value) {
        return Validator.isMactchRegex(INT_NUMBER_EXCLUDE_ZERO, value);
    }

    /**
     * 是否正整数
     *
     * <ul>
     *     <li>{@code isPositiveNumber("123")} 返回 true </li>
     *     <li>{@code isPositiveNumber("1.23")} 返回 false </li>
     *     <li>{@code isPositiveNumber("-1.23")} 返回 false </li>
     *     <li>{@code isPositiveNumber("0")} 返回 false </li>
     *     <li>{@code isPositiveNumber("abc")} 返回 false </li>
     * </ul>
     *
     * @param value 需要校验的字符
     * @return 正整数返回 true，否则返回 false
     */
    public static boolean isPositiveInt (CharSequence value) {
        return Validator.isMactchRegex(POSITIVE_INT_NUMBER, value);
    }

    /**
     * 是否非负整数(大于等于零的整数)
     *
     * <ul>
     *     <li>{@code isPositiveNumber("123")} 返回 true </li>
     *     <li>{@code isPositiveNumber("0")} 返回 true </li>
     *     <li>{@code isPositiveNumber("1.23")} 返回 false </li>
     *     <li>{@code isPositiveNumber("-123")} 返回 false </li>
     *     <li>{@code isPositiveNumber("abc")} 返回 false </li>
     * </ul>
     *
     * @param value 需要校验的字符
     * @return 正整数和零返回 true，否则返回 false
     */
    public static boolean isIntGteZero (CharSequence value) {
        return Validator.isMactchRegex(INT_NUMBER_GTE_ZERO, value);
    }

    /**
     * 是否负整数
     *
     * <ul>
     *     <li>{@code isPositiveNumber("-123")} 返回 true </li>
     *     <li>{@code isPositiveNumber("123")} 返回 false </li>
     *     <li>{@code isPositiveNumber("0")} 返回 false </li>
     *     <li>{@code isPositiveNumber("abc")} 返回 false </li>
     * </ul>
     *
     * @param value 需要校验的字符
     * @return 负整数返回 true，否则返回 false
     */
    public static boolean isNegativeInt (CharSequence value) {
        return Validator.isMactchRegex(NEGATIVE_INT_NUMBER, value);
    }

    /**
     * 是否非正整数(小于等于零的整数)
     *
     * <ul>
     *     <li>{@code isPositiveNumber("-123")} 返回 true </li>
     *     <li>{@code isPositiveNumber("-0")} 返回 true </li>
     *     <li>{@code isPositiveNumber("123")} 返回 false </li>
     *     <li>{@code isPositiveNumber("abc")} 返回 false </li>
     * </ul>
     *
     * @param value 需要校验的字符
     * @return 负整数和零返回 true，否则返回 false
     */
    public static boolean isIntLteZero (CharSequence value) {
        return Validator.isMactchRegex(INT_NUMBER_LTE_ZERO, value);
    }

    /**
     * 最多保留两位小数的正数或0
     *
     * <ul>
     *     <li>{@code isFloatScaleTwoGteZero("5")} 返回 true </li>
     *     <li>{@code isFloatScaleTwoGteZero("3.")} 返回 false </li>
     *     <li>{@code isFloatScaleTwoGteZero("6.18")} 返回 true </li>
     *     <li>{@code isFloatScaleTwoGteZero("8.234")} 返回 false </li>
     * </ul>
     *
     * @param value 需要校验的字符
     * @return
     */
    public static boolean isFloatScaleTwoGtZero (CharSequence value) {
        if (!Validator.isMactchRegex(FLOAT_SCALE_TWO_GTE_ZERO, value)) {
            return false;
        }
        return Double.parseDouble((String)value) > 0D;
    }

    /**
     * 是否正整数，且在指定范围内，范围为左右闭区间
     *
     * @param value 需要校验的字符
     * @param begin 开始范围
     * @return end 结束范围
     */
    public static boolean isIntBetweenAnd(CharSequence value, Integer begin, Integer end) {

        if (value == null || !NumberUtils.isInteger(value.toString())) {
            return false;
        }
        Integer v = Integer.valueOf(value.toString());
        return v >= begin && v <= end;
    }
}
