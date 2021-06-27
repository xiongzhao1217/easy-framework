package com.easy.framework.excel.domain.upload;

import com.easy.framework.core.exception.InvalidParamException;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 上传实体对象，对应每一行excel数据
 *
 * <p>
 * excel上传时，如果需要记录每一行失败的原因，excel行对应的java对象可以继承该基类
 * </p>
 *
 * @author xiongzhao1
 * @date 2019-12-20
 */
@Setter
@Getter
public abstract class BaseRow implements Serializable {

    /**
     * 序列化id
     */
    private static final long serialVersionUID = 6043759651668524252L;

    /**
     * 单行数据处理失败原因
     */
    private String errorMessage;

    /**
     * 参数校验方法
     */
    public void doValidate() {
        try {
            this.validate();
        } catch (Exception e) {
            throw new InvalidParamException(e.getMessage());
        }
    }

    /**
     * 需要子类实现的参数自定义校验
     * <p>
     *     子类校验失败需要抛异常
     * </p>
     */
    public abstract void validate();
}

