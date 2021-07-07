package com.easy.framework.core.domain.rpc.request;

import com.easy.framework.core.exception.InvalidParamException;
import org.apache.commons.lang3.Validate;

import java.io.Serializable;

/**
 * RPC接口入参父类，包含内控合规参数
 *
 * @author xiongzhao
 * @date 2021/06/15
 */
public abstract class BaseRequest implements Validator, Serializable {

    /**
     * 序列化id
     */
    private static final long serialVersionUID = 7075403590581379341L;

    /**
     * 调用方系统编码
     */
    private String appCode;
    /**
     * 调用方操作人名称
     */
    private String operator;
    /**
     * 调用方IP
     */
    private String operatorIp;
    /**
     * 调用方端口
     */
    private String operatorPort;
    /**
     * 请求Id,RPC接口中每次打印日志时,需要把requestId打印出来,方便后续问题排查
     */
    private String requestId;

    /**
     * 参数校验方法
     */
    public void doValidate() {
        try {
            Validate.notBlank(appCode, "appCode不能为空");
            Validate.notBlank(operator, "operator不能为空");
            Validate.notNull(requestId, "requestId不能为空");
            this.validate();
        } catch (Exception e) {
            throw new InvalidParamException(e.getMessage());
        }
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperatorIp() {
        return operatorIp;
    }

    public void setOperatorIp(String operatorIp) {
        this.operatorIp = operatorIp;
    }

    public String getOperatorPort() {
        return operatorPort;
    }

    public void setOperatorPort(String operatorPort) {
        this.operatorPort = operatorPort;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "BaseRequest{" +
                ", appCode='" + appCode + '\'' +
                ", operator='" + operator + '\'' +
                ", operatorIp='" + operatorIp + '\'' +
                ", operatorPort='" + operatorPort + '\'' +
                ", requestId='" + requestId + '\'' +
                '}';
    }
}

