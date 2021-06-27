package com.easy.framework.core.exception;

import com.easy.framework.core.enums.EnumInterface;
import com.easy.framework.core.enums.HttpResultEnum;

/**
 * 服务（业务）异常如“ 账号或密码错误 ”，该异常只做INFO级别的日志记录
 *
 * <p>
 * 自定义基类异常，它是一个运行时异常，ManagerException、ServiceException、MessageException等均继承该异常。
 * </p>
 *
 * @author xiongzhao
 * @date 2020/06/11
 */
public class AppException extends RuntimeException {
    /**
     * 错误码 {@link HttpResultEnum}
     */
    private Integer code;

    /**
     * 默认构造
     */
    public AppException() {
        super();
    }

    /**
     * code码默认500
     * @param message
     */
    public AppException(String message) {
        super(message);
        this.code = HttpResultEnum.FAIL.getCode();
    }

    public AppException(EnumInterface<Integer> resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public AppException(EnumInterface<Integer> resultEnum, String message) {
        super(message);
        this.code = resultEnum.getCode();
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
        this.code = HttpResultEnum.FAIL.getCode();
    }

    public AppException(EnumInterface<Integer> resultEnum, String message, Throwable cause) {
        super(message, cause);
        this.code = resultEnum.getCode();
    }

    public AppException(String format, Object ...args) {
        super(String.format(format, args));
        this.code = HttpResultEnum.FAIL.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
