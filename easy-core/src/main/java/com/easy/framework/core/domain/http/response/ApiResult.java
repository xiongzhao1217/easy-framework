package com.easy.framework.core.domain.http.response;

import com.easy.framework.core.enums.EnumInterface;
import com.easy.framework.core.enums.HttpResultEnum;
import com.easy.framework.core.exception.AppException;

/**
 * 统一API响应结果封装
 *
 * <p>
 *     core包中定义了{@link HttpResultEnum} 返回码枚举类,如果该类已满足业务诉求,可直接使用,
 *     否则,可自定义一个实现自{@link EnumInterface}接口的枚举类,要求成功code码必须为0,系统
 *     异常code码必须为500.
 * </p>
 *
 * @see AppException
 * @see EnumInterface
 * @see HttpResultEnum
 *
 * @author xiongzhao
 * @date 2020/06/11
 */
public class ApiResult {
    /**
     * 状态码，0为成功
     * 可以复用{@link HttpResultEnum}，如果需要扩展，需要自己建一个枚举类，并实现{@link EnumInterface}
     */
    private Integer code;
    /**
     * 错误信息
     */
    private String message;
    /**
     * 接口返回业务数据
     */
    private Object data;

    /**
     * 成功且不需要返回业务数据
     * @return 没有业务数据的 {@link ApiResult}
     */
    public static ApiResult success() {
        return success(null);
    }

    /**
     * 成功且需要返回业务数据
     * 成功，code默认取 {@link HttpResultEnum#SUCCESS}, message可自定义
     * @return 有业务数据的 {@link ApiResult}
     */
    public static ApiResult success(Object data) {
        return new ApiResult()
                .setCode(HttpResultEnum.SUCCESS.getCode())
                .setMessage(HttpResultEnum.SUCCESS.getMessage())
                .setData(data);
    }

    /**
     * 失败，通过 {@link EnumInterface} 枚举来指定 code 和 message
     * @return {@link ApiResult}
     */
    public static ApiResult error(EnumInterface<Integer> code) {
        return error(code.getCode(), code.getMessage());
    }

    /**
     * 失败，code默认取 {@link HttpResultEnum#FAIL}, message可自定义
     * @return {@link ApiResult}
     */
    public static ApiResult error(String message) {
        return error(HttpResultEnum.FAIL, message);
    }

    /**
     * 失败，code默认取 {@link EnumInterface#getCode()}, message可自定义
     * @return {@link ApiResult}
     */
    public static ApiResult error(EnumInterface<Integer> code, String message) {
        return error(code.getCode(), message);
    }

    /**
     * 失败，通过 {@link AppException} 异常来指定 code 和 message
     * @return {@link ApiResult}
     */
    public static ApiResult error(AppException ae) {
        return error(ae.getCode(), ae.getMessage());
    }

    /**
     * 失败，自定义code和message，私有方法不允许直接调用
     * @return {@link ApiResult}
     */
    private static ApiResult error(Integer code, String message) {
        return new ApiResult()
                .setCode(code)
                .setMessage(message);
    }

    public Integer getCode() {
        return code;
    }

    public ApiResult setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ApiResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public ApiResult setData(Object data) {
        this.data = data;
        return this;
    }
}

