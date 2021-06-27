package com.easy.framework.core.enums;

/**
 * 数据请求结果枚举类
 *
 * @author xiongzhao
 * @date 2020/11/9
 */
public enum RpcResultEnum implements EnumInterface<Integer>{
    /**
     * 成功状态
     */
    SUCCESS(0, "调用成功"),
    /**
     * 参数校验错误
     */
    PARAMETER_ERROR(101, "参数错误"),

    UNKNOWN_ERROR(200, "未知错误"),

    UN_LOGIN(300, "用户未登录"),

    UN_CORRECT_SQL(400, "SQL语句不合要求"),

    SYSTEM_ERROR(500, "系统错误"),

    SERVICE_DUPLICATE_EXCEPTION(514, "唯一索引重复"),

    OUTER_SERVICE_EXCEPTION(999, "外部服务异常"),;

    /**
     * 结果状态码
     */
    private Integer code;
    /**
     * 结果信息
     */
    private String message;

    /**
     * 构造方法
     *
     * @param code    结果状态码
     * @param message 结果信息
     */
    RpcResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

