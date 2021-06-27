package com.easy.framework.excel.domain.upload;

import com.easy.framework.core.exception.AppException;
import com.easy.framework.excel.domain.common.Task;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * excel 上次上下文，用于参数传递，任务进度同步，记录处理失败的数据行
 * @param <T> excel行数据对应的java对象
 *
 * @author xiongzhao
 * @date 2019-12-17
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class UploadContext<T extends BaseRow> {

    /**
     * 操作人
     */
    private String operator;

    /**
     * 上传的excel文件
     */
    private MultipartFile file;

    /**
     * 任务执行进度
     */
    private Task task;

    /**
     * 任务执行失败列表
     */
    private List<T> failList;

    /**
     * 不允许调用空构造
     */
    private UploadContext() {
        throw new AppException("UploadContext不允许调用空构造");
    }

    /**
     * 构造
     */
    public UploadContext(String operator, MultipartFile file) {
        this.operator = operator;
        this.file = file;
        task = new Task(operator);
    }

    /**
     * 获取任务id
     * @return
     */
    public String getTaskId () {
        if (task == null) {
            task = new Task(operator);
        }
        return task.getTaskId();
    }

    /**
     * 添加到失败列表
     * @param t
     */
    public void addFail(T t, String message) {
        if (CollectionUtils.isEmpty(failList)) {
            failList = Lists.newArrayListWithExpectedSize(100);
        }
        t.setErrorMessage(message);
        failList.add(t);
    }

    /**
     * 添加到失败列表, t已经包含了errorMessage
     * @param t
     */
    public void addFail(T t) {
        addFail(t, t.getErrorMessage());
    }

    /**
     * 批量添加到失败列表
     * @param fails
     */
    public void addFails(List<T> fails, String message) {
        if (CollectionUtils.isEmpty(failList)) {
            failList = Lists.newArrayListWithExpectedSize(100);
        }
        if (StringUtils.isNotEmpty(message)) {
            fails.forEach(item -> item.setErrorMessage(message));
        }
        failList.addAll(fails);
    }

    /**
     * 批量添加到失败列表, t已经包含了errorMessage
     * @param fails
     */
    public void addFails(List<T> fails) {
        addFails(fails, null);
    }

    /**
     * 获取失败的行数
     * @return
     */
    public long getFailSize() {
        if (CollectionUtils.isEmpty(failList)) {
            return 0L;
        }
        return failList.size();
    }
}

