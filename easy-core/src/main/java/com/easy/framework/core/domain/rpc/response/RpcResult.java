package com.easy.framework.core.domain.rpc.response;

import com.easy.framework.core.enums.EnumInterface;
import com.easy.framework.core.enums.RpcResultEnum;

import java.io.Serializable;
import java.util.List;

/**
 * RPC通用返回体
 * @author xiongzhao
 * @date 2021/06/11
 * @param <T>
 */
public class RpcResult<T> implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1360359681132053257L;

    /**
     * success
     */
    private boolean success;

    /**
     * code
     */
    private Integer code;

    /**
     * message
     *
     */
    private String message;

    /**
     * data
     */
    private T data;

    /**
     * 成功列表
     * <p>
     *     批量操作,处理成功的数据列表
     * </p>
     */
    private List<T> successList;

    /**
     * 失败列表
     * <p>
     *     批量操作,处理失败的数据列表
     * </p>
     */
    private List<T> failList;


    /**
     * 无data success
     *
     * @param <E>
     * @return
     */
    public static <E> RpcResult<E> success() {
        return (new RpcResult<E>()).setSuccess(true).setCode(RpcResultEnum.SUCCESS.getCode());
    }

    /**
     * 有data success
     * @param e
     * @param <E>
     * @return
     */
    public static <E> RpcResult<E> success(E e) {
        return (new RpcResult<E>()).setSuccess(true).setCode(RpcResultEnum.SUCCESS.getCode()).setData(e);
    }

    /**
     * 批量操作 success
     * @param successList
     * @param failList
     * @param <T>
     * @return
     */
    public static <T> RpcResult<T> success(List<T> successList, List<T> failList) {
        return new RpcResult<T>()
                .setSuccess(true)
                .setCode(RpcResultEnum.SUCCESS.getCode())
                .setMessage(RpcResultEnum.SUCCESS.getMessage())
                .setSuccessList(successList)
                .setFailList(failList);
    }


    /**
     * 接口调用失败
     * @param message
     * @param <E>
     * @return
     */
    public static <E> RpcResult<E> error(String message) {
        return error(RpcResultEnum.SYSTEM_ERROR.getCode(), message, null);
    }

    /**
     * 接口调用失败
     * @param enumInterface
     * @param <E>
     * @return
     */
    public static <E> RpcResult<E> error(EnumInterface<Integer> enumInterface) {
        return error(enumInterface.getCode() != null ? enumInterface.getCode() : null, enumInterface.getMessage(), null);
    }

    /**
     * 带错误码、错误消息
     *
     * @param code
     * @param message
     * @param <E>
     * @return
     */
    public static <E> RpcResult<E> error(Integer code, String message) {
        return error(code, message, null);
    }

    /**
     * 带异常
     *
     * @param code
     * @param message
     * @param e
     * @param <E>
     * @return
     */
    public static <E> RpcResult<E> error(Integer code, String message, E e) {
        return (new RpcResult<E>()).setSuccess(false).setCode(code).setMessage(message).setData(e);
    }

    /**
     * isSuccess
     * @return
     */
    public boolean isSuccess() {
        return this.success;
    }

    /**
     * setSuccess
     *
     * @param success
     * @return
     */
    public RpcResult<T> setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    /**
     * getCode
     *
     * @return
     */
    public Integer getCode() {
        return this.code;
    }

    /**
     * setCode
     *
     * @param code
     * @return
     */
    public RpcResult<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    /**
     * getMessage
     *
     * @return
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * setMessage
     *
     * @param message
     * @return
     */
    public RpcResult<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * getData
     *
     * @return
     */
    public T getData() {
        return this.data;
    }

    /**
     * setData
     *
     * @param data
     * @return
     */
    public RpcResult<T> setData(T data) {
        this.data = data;
        return this;
    }

    public List<T> getSuccessList() {
        return successList;
    }

    public RpcResult<T> setSuccessList(List<T> successList) {
        this.successList = successList;
        return this;
    }

    public List<T> getFailList() {
        return failList;
    }

    public RpcResult<T> setFailList(List<T> failList) {
        this.failList = failList;
        return this;
    }

    @Override
    public String toString() {
        return "RpcResult{" +
                "success=" + success +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", successList=" + successList +
                ", failList=" + failList +
                '}';
    }
}

