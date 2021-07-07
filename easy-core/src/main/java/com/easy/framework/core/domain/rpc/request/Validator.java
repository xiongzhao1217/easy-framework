package com.easy.framework.core.domain.rpc.request;

import com.easy.framework.core.exception.InvalidParamException;

/**
 * RPC接口入参父类，包含内控合规参数
 *
 * @author xiongzhao
 * @date 2021/06/15
 */
public interface Validator {

    /**
     * 需要子类实现的自定义校验
     * 校验不通过需要抛{@link InvalidParamException}异常
     */
    void validate() throws InvalidParamException;
}
