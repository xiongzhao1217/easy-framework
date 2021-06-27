package com.easy.framework.core.enums;

/**
 * 通用枚举接口
 * @param <T> 枚举值类型
 *
 * @author xiongzhao
 * @date 2020/06/11
 */
public interface EnumInterface<T> {

    /**
     * 获取枚举值
     * @return
     */
    T getCode();

    /**
     * 获取名称描述
     * @return
     */
    String getMessage();
}
