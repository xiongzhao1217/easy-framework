package com.easy.framework.util;

import com.google.common.base.Strings;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * 字符串工具类
 *
 * <p>
 * 继承apache common-lang3包的StringUtils，相关方法可直接使用<br>
 * 合并google guava包中的Strings差异方法<br>
 * 添加自定义的常用字符串处理方法
 * </p>
 *
 * @see org.apache.commons.lang3.StringUtils
 * @see com.google.common.base.Strings
 * @author xiongzhao
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 私有构造，禁止 new 对象
     */
    private StringUtils () {}

    /**
     * 如果传入的字符串为null，返回""，否则返回自己
     * @param string 字符串
     * @return 如果 {@code string} 为null，返回""，否则返回自己
     */
    public static String nullToEmpty(@Nullable String string) {
        return Strings.nullToEmpty(string);
    }

    /**
     * 如果传入的字符串为""，返回null，否则返回自己
     * @param string 字符串
     * @return 如果 {@code string} 为""，返回null，否则返回自己
     */
    public static @Nullable String emptyToNull(@Nullable String string) {
        return Strings.emptyToNull(string);
    }

    /**
     * 用字符 {@code padChar} 重复填充在字符串 {@code string} 前面，直到达到指定长度 {@code minLength}
     * 如果指定的长度比字符串本身长度小，返回字符串本身
     *
     * <ul>
     *   <li>{@code padStart("7", 3, '0')} 返回 {@code "007"}
     *   <li>{@code padStart("2010", 3, '0')} 返回 {@code "2010"}
     * </ul>
     *
     * @param string 需要填充的原始字符串
     * @param minLength 对目标字符串填充到指定的长度
     * @param padChar 填充的字符
     * @return 处理后的字符串
     */
    public static String padStart(String string, int minLength, char padChar) {
        return Strings.padStart(string, minLength, padChar);
    }

    /**
     * 用字符 {@code padChar} 重复填充在字符串 {@code string} 末尾，直到达到指定长度 {@code minLength}
     * 如果指定的长度比字符串本身长度小，返回字符串本身
     *
     * <ul>
     *   <li>{@code padEnd("4.", 5, '0')} 返回 {@code "4.000"}
     *   <li>{@code padEnd("2010", 3, '!')} 返回 {@code "2010"}
     * </ul>
     *
     * @param string 需要填充的原始字符串
     * @param minLength 对目标字符串填充到指定的长度
     * @param padChar 填充的字符
     * @return 处理后的字符串
     */
    public static String padEnd(String string, int minLength, char padChar) {
        return Strings.padEnd(string, minLength, padChar);
    }

    /**
     * 对字符串 {@code string} 重复拼接 {@code count} 次
     *
     * <ul>
     *     <li>{@code repeat("hi", 3)} 返回 {@code hihihi}</li>
     *     <li>{@code repeat("", 3)} 返回 {@code ""}</li>
     *     <li>{@code repeat(null, 3)} 返回 {@code NullPointException}</li>
     * </ul>
     * @param string 非空的字符串
     * @param count 重复次数
     * @return 处理后的字符串
     */
    public static String repeat(String string, int count) {
        return Strings.repeat(string, count);
    }
}
