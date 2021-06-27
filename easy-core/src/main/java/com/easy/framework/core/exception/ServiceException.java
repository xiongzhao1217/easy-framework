package com.easy.framework.core.exception;

import com.easy.framework.core.enums.HttpResultEnum;

/**
 * Service模块异常，继承自 {@link AppException}
 *
 * @author xiongzhao
 * @date 2020/06/11
 */
public class ServiceException extends AppException {
    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(HttpResultEnum errorCode) {
        super(errorCode);
    }

    public ServiceException(HttpResultEnum errorCode, String message) {
        super(errorCode, message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(HttpResultEnum errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public ServiceException(String format, Object ...args) {
        super(format, args);
    }
}

