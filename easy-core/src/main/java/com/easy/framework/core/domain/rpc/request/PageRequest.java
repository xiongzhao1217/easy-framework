package com.easy.framework.core.domain.rpc.request;

import com.easy.framework.core.exception.InvalidParamException;
import org.apache.commons.lang3.Validate;

import java.io.Serializable;

/**
 * RPC分页接口入参父类
 *
 * @author xiongzhao
 * @date 2021/06/15
 */
public abstract class PageRequest extends BaseRequest implements Serializable {

    /**
     * 序列化id
     */
    private static final long serialVersionUID = 1420431368286571093L;

    /**
     * 页码
     */
    private Integer pageNum;
    /**
     * 页大小
     */
    private Integer pageSize;

    /**
     * 参数校验方法
     */
    @Override
    public void doValidate() {
        try {
            super.doValidate();
            Validate.notNull(pageNum, "pageNum不能为空");
            Validate.notNull(pageSize, "pageSize不能为空");
            if (pageNum <= 0) {
                pageNum = 1;
            }
            // 这里500较大,如果业务要求比较小,可在子类validate方法中进行额外校验
            if (pageSize > 500) {
                throw new InvalidParamException("pageSize数量不能超过最大值" + 500);
            }
        } catch (Exception e) {
            throw new InvalidParamException(e.getMessage());
        }
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "PageRequest{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", appCode='" + getAppCode() + '\'' +
                ", operator='" + getOperator() + '\'' +
                ", operatorIp='" + getOperatorIp() + '\'' +
                ", operatorPort='" + getOperatorPort() + '\'' +
                ", requestId='" + getRequestId() + '\'' +
                '}';
    }
}

