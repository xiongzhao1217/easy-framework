package com.easy.framework.core.enums;
/**
 * 响应码枚举，参考HTTP状态码的语义
 *
 * <p>
 * web项目使用，可以根据业务需要进行添加。<br>
 * 约定：业务执行成功的状态码为0，未认证(未登录)的状态码为401。
 * </p>
 *
 * @author xiongzhao
 * @date 2020/06/11
 */
public enum HttpResultEnum implements EnumInterface<Integer> {
    /**
     * 成功
     */
    SUCCESS(0, "成功"),
    /**
     * 失败
     */
    FAIL(500, "系统错误"),
    /**
     * 接口无权访问
     */
    UNAUTHORIZED(301, "接口无权访问"),
    /**
     * 未认证
     */
    TOKEN_EXPRIED(401, "未认证，需要登录"),
    /**
     * 接口不存在
     */
    NOT_FOUND(404, "接口不存在"),
    ;

    /**
     * 编码
     */
    private final int code;

    /**
     * 错误信息
     */
    private final String message;

    /**
     * 枚举构造
     * @param code 状态码
     * @param name 状态描述
     */
    HttpResultEnum(int code, String name) {
        this.code = code;
        this.message = name;
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

