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
![微服务入参](https://github.com/xiongzhao1217/markdown-photos/blob/master/easy-framework/rpc-request.png?raw=true)

2. 出参用法
![微服务出参](https://github.com/xiongzhao1217/markdown-photos/blob/master/easy-framework/rpc-response.png?raw=true)

```java
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
}
```
