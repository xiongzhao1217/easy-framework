package com.easy.framework.core.exception;

/**
 * 非法参数异常
 *
 * @author xiongzhao
 * @date 2021/06/15
 */
public class InvalidParamException extends AppException {

    /**
     * 无参构造方法
     */
    public InvalidParamException() {
        super();
    }

    /**
     * 构造方法，仅包含自定义message
     */
    public InvalidParamException(String message) {
        super(message);
    }

    /**
     * 构造方法，用Throwable构造一个信息的异常
     */
    public InvalidParamException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

    /**
     * 构造方法，自定义message和Throwable
     */
    public InvalidParamException(String message, Throwable cause) {
        super(message, cause);
    }
}

