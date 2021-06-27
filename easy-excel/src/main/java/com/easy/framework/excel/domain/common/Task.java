package com.easy.framework.excel.domain.common;

import cn.hutool.core.util.IdUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 任务进度
 *
 * @author xiongzhao
 * @date 2020/11/21
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class Task {

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 总数
     */
    private Integer total;

    /**
     * 处理成功的行数
     */
    private Integer success;

    /**
     * 已经处理的总数
     */
    private Integer processedNums;

    /**
     * 是否还在运行
     */
    private Boolean isRunning;

    /**
     * 是否异常中断
     */
    private Boolean interrupted;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 任务开始时间
     */
    private Long startTime;

    /**
     * 任务结束时间
     */
    private Long endTime;

    /**
     * 异常中断时的错误信息
     */
    private String message;

    /**
     * 默认构造
     */
    public Task() {}

    /**
     * 初始化任务
     * @param operator
     */
    public Task(String operator) {
        this(0, operator);
    }

    /**
     * 初始化任务
     * @param total
     * @param operator
     */
    public Task(int total, String operator) {
        this.total = total;
        this.success = 0;
        this.processedNums = 0;
        this.operator = operator;
        this.isRunning = true;
        this.interrupted = false;
        this.startTime = System.currentTimeMillis();
        taskId = new StringBuilder("upload.excel.")
                .append(operator).append(".")
                .append(IdUtil.fastSimpleUUID()).toString();
    }

    /**
     * 增加任务完成数
     * @param nums
     */
    public Task addProgress (int nums) {
        this.processedNums += nums;
        if(processedNums >= total){
            this.isRunning = false;
        }
        return this;
    }

    /**
     * 增加任务成功数
     * @param nums
     */
    public Task addSuccess (int nums) {
        this.success += nums;
        return this;
    }

    /**
     * 任务异常中断
     * @param message
     */
    public void interruptedTask (String message) {
        this.interrupted = true;
        this.message = message;
        this.isRunning = false;
    }
}

