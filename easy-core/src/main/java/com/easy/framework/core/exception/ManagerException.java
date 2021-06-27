package com.easy.framework.core.exception;

import com.easy.framework.core.enums.HttpResultEnum;

/**
 * Manager模块异常，继承自 {@link AppException}
 *
 * @author xiongzhao
 * @date 2020/06/11
 */
public class ManagerException extends AppException {
    public ManagerException() {
        super();
    }

    public ManagerException(String message) {
        super(message);
    }

    public ManagerException(HttpResultEnum errorCode) {
        super(errorCode);
    }

    public ManagerException(HttpResultEnum errorCode, String message) {
        super(errorCode, message);
    }

    public ManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ManagerException(HttpResultEnum errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public ManagerException(String format, Object ...args) {
        super(format, args);
    }
}

