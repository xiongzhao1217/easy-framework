package com.easy.framework.excel.service.upload;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import com.easy.framework.excel.domain.common.Task;
import com.easy.framework.excel.domain.upload.BaseRow;
import com.easy.framework.excel.domain.upload.UploadContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

/**
 * excel上传接口
 *
 * <p>
 * 入口方法为{@link ExcelUploadService#execute(UploadContext)} (UploadContext)}
 * </p>
 *
 * @param <T> excel行数据对应的实体类, 继承 {@link BaseRow} 用于记录每一行处理失败的原因
 * @param <C> 上传上下文，继承 {@link UploadContext}，包含基本的字段，可根据业务场景定义子类进行扩展
 *
 * @author xiongzhao1
 * @date 2019-12-18
 */
public interface ExcelUploadService<T extends BaseRow, C extends UploadContext<T>> {

    /**
     * 方法执行入口
     * @param context
     */
    void execute(C context);

    /**
     * 方法执行入口
     *
     * <p>
     *     解析excel同步执行，执行具体业务逻辑异步处理
     *     使用默认线程池，核心线程数4，最大线程数10，队列大小100
     * </p>
     *
     * @param context
     */
    void asyncExecute(C context);

    /**
     * 方法执行入口
     *
     * <p>
     *     解析excel同步执行，执行具体业务逻辑异步处理
     * </p>
     *
     * @param context
     * @param executor 自定义线程池
     */
    void asyncExecute(C context, ExecutorService executor);

    /**
     * 校验context，包含参数非空判断、有效性判断
     *
     * <p>
     * 抽象类已经实现了基本的判断，如果业务有额外判断场景，可以主动实现该接口并super抽象接口实现
     * </p>
     * @param context
     */
    void checkContext(C context);

    /**
     * excel 转 对象列表
     * @param reader
     * @param t
     * @return
     */
    List<T> readAll(ExcelReader reader, Class<T> t);

    /**
     * 数据去重key
     *
     * 子类若不实现，则不进行去重
     * @return
     */
    default Function<T, Object> deDuplicationKey() {
        return null;
    }

    /**
     * 出现重复错误提示
     * @return
     */
    default String duplicationErrorMessage() {
        return null;
    }

    /**
     * 进行业务校验并过滤掉不符合条件的数据
     * 不符合条件的需要添加到错误列表中，并记录原因
     * @param list
     * @param context
     * @return 有效的列表
     */
    List<T> filterList(List<T> list, C context);

    /**
     * 业务逻辑处理
     * @param list
     * @param context
     * @return 返回成功处理的数量
     */
    int handle(List<T> list, C context);

    /**
     * 获取任务进度，建议每隔5秒查询一次
     * @param taskId
     * @return
     */
    Task getTask(String taskId);

    /**
     * 导出失败列表
     *
     * <p>
     * 抽象父类已实现该方法
     * </p>
     * @param taskId
     * @param request
     * @param response
     */
    void exportFailExcel(String taskId, HttpServletRequest request, HttpServletResponse response);

    /**
     * 指定excel标题别名，即bean字段与标题的对应关系
     */
    LinkedHashMap<String, String> addHeaderAlias();

    /**
     * 处理失败的业务数据写入excel
     * @param writer
     * @param failList
     */
    void writeFailExcel(ExcelWriter writer, List<T> failList);

    /**
     * 指定分片大小，指定后会按照分片大小分批次调用handle方法
     * <p>
     *     eg: excel共1000行,若指定partSize=50,则分1000 / 50 = 20次调用handle方法
     *     一个批次中的数据,可进行批量操作,建议根据外部接口或自己的接口单次调用支持的最大数量进行设置
     * </p>
     * @return
     */
    int partSize();

    /**
     * excel最大允许上传的行数
     * @return
     */
    int getMaxSize();

    /**
     * 失败文件名称，默认失败列表+年月日时分秒
     * @return
     */
    String failFileName();
}

