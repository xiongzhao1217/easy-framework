## 简介

## 组件
* [easy-core包](https://git.jd.com/b-gms/bgms-common/wikis/%E9%87%91%E9%BC%8Ecore%E5%8C%85)
* [easy-util包](https://git.jd.com/b-gms/bgms-common/wikis/%E9%87%91%E9%BC%8Erpc%E5%8C%85)
* [easy-excel包]()

## easy-core
#### 简介
包含通用base、domain、enum、rpc、exception等。
#### enum包
包含通用枚举接口和一个Http返回码的枚举实现，业务系统可基于 `BaseEnum` 接口实现自定义枚举，也可以直接使用 `ResultCode` 枚举
![枚举类图](https://github.com/xiongzhao1217/markdown-photos/blob/master/easy-framework/enum.png)
#### exception包
包含通用自定义异常， 继承自 `RuntimeException` 的 `AppException`, 继承自 `AppException` 的 `ManagerException`, `ServiceException`, `MessageException`，项目中的模块可抛出对应的业务异常。
![异常类图](https://github.com/xiongzhao1217/markdown-photos/blob/master/easy-framework/exception.png?raw=true)
#### rpc包
1. 入参用法
<br>
![微服务入参](https://github.com/xiongzhao1217/markdown-photos/blob/master/easy-framework/rpc-request.png?raw=true)

2. 出参用法
<br>
![微服务出参](https://github.com/xiongzhao1217/markdown-photos/blob/master/easy-framework/rpc-response.png?raw=true)

```java
/**
 * RPC公用返还结果
 * @author xiongzhao1
 * @date 2021/06/11
 * @param <T>
 */
public class RpcResult<T> implements Serializable{

	/**
	 * success
	 */
	private boolean success;

	/**
	 * code
	 */
	private String code;

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
	 * 无参success
	 *
	 * @param <E>
	 * @return
	 */
	public static <E> RpcResult<E> success() {
		return (new RpcResult<E>()).setSuccess(true);
	}

	/**
	 * 异常success
	 * @param e
	 * @param <E>
	 * @return
	 */
	public static <E> RpcResult<E> success(E e) {
		return (new RpcResult<E>()).setSuccess(true).setData(e);
	}

	/**
	 * 接口调用失败
	 * @param message
	 * @param <E>
	 * @return
	 */
	public static <E> RpcResult<E> error(String message) {
		return error(null, message, null);
	}

	/**
	 * 接口调用失败
	 * @param enumInterface
	 * @param <E>
	 * @return
	 */
	public static <E> RpcResult<E> error(EnumInterface enumInterface) {
		return error(enumInterface.code() != null ? enumInterface.code().toString() : null, enumInterface.getMessage(), null);
	}

	/**
	 * 带错误码、错误消息
	 *
	 * @param code
	 * @param message
	 * @param <E>
	 * @return
	 */
	public static <E> RpcResult<E> error(String code, String message) {
		return error(code, message, null);
	}

	/**
	 * failure 异常
	 *
	 * @param code
	 * @param message
	 * @param e
	 * @param <E>
	 * @return
	 */
	public static <E> RpcResult<E> error(String code, String message, E e) {
		return (new RpcResult<E>()).setSuccess(false).setCode(code).setMessage(message).setData(e);
	}
}
```

```java
/**
 * 批量操作rpc返回结果
 *
 * <p>
 *     中断性错误，调用error静态方法，如前置参数校验错误
 *     否则调用success静态方法，分别返回处理成功的数据successList和处理失败的数据failList
 * </p>
 *
 * @author xiongzhao
 * @date 2021/06/15
 */
public class RpcListResult<T> implements Serializable {

    /**
     * success
     */
    private boolean success;
    
    /**
     * code
     */
    private String code;
    
    /**
     * message
     */
    private String message;
    
    /**
     * 成功列表
     */
    private List<T> successList;
    
    /**
     * 失败列表
     */
    private List<T> failList;

    /**
     * 调用成功返回
     */
    public static <T> RpcListResult<T> success(List<T> successList, List<T> failList) {
        return new RpcListResult<T>()
                .setSuccess(true)
                .setCode(SystemConstant.DEFAULT_SUCCESS_CODE)
                .setMessage(SystemConstant.DEFAULT_SUCCESS_MESSAGE)
                .setSuccessList(successList)
                .setFailList(failList);
    }

    /**
     * 接口调用失败
     * @param message
     * @param <T>
     * @return
     */
    public static <T> RpcListResult<T> error(String message) {
        return error(SystemConstant.DEFAULT_ERROR_CODE, message);
    }

    /**
     * 接口调用失败
     * @param enumInterface
     * @param <T>
     * @return
     */
    public static <T> RpcListResult<T> error(EnumInterface enumInterface) {
        return error(enumInterface.code() != null ? enumInterface.code().toString() : null, enumInterface.getMessage());
    }

    /**
     * 带错误码、错误消息
     *
     * @param code
     * @param message
     * @return
     */
    public static RpcListResult error(String code, String message) {
        return new RpcListResult()
                .setSuccess(false)
                .setCode(code)
                .setMessage(message);
    }
}
```
