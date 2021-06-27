package com.easy.framework.core.exception;

import com.easy.framework.core.enums.HttpResultEnum;

/**
 * Message模块异常，继承自 {@link AppException}
 *
 * @author xiongzhao
 * @date 2020/06/11
 */
public class MessageException extends AppException {
    public MessageException() {
        super();
    }

    public MessageException(String message) {
        super(message);
    }

    public MessageException(HttpResultEnum errorCode) {
        super(errorCode);
    }

    public MessageException(HttpResultEnum errorCode, String message) {
        super(errorCode, message);
    }

    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageException(HttpResultEnum errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public MessageException(String format, Object ...args) {
        super(format, args);
    }
}

