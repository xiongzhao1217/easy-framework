package com.easy.framework.excel.service.upload;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.easy.framework.core.exception.ServiceException;
import com.easy.framework.excel.constant.CacheConstant;
import com.easy.framework.excel.domain.common.Task;
import com.easy.framework.excel.domain.upload.BaseRow;
import com.easy.framework.excel.domain.upload.UploadContext;
import com.easy.framework.excel.service.common.CacheService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.easy.framework.excel.constant.CacheConstant.UPLOAD_EXCEL_PROGRESS;
import static java.util.stream.Collectors.toList;

/**
 * excel上传服务抽象类
 *
 * @param <T> excel行数据对应的实体类
 * @param <C> 上传上下文，继承 {@link UploadContext}，包含基本的字段，可根据业务场景定义子类进行扩展
 *
 * @author xiongzhao1
 * @date 2020-11-21
 */
@Slf4j
abstract public class AbstractExcelUploadService<T extends BaseRow, C extends UploadContext<T>> implements ExcelUploadService<T, C> {

    /**
     * The cache service
     */
    @Resource
    private CacheService cacheService;

    /**
     * 实体泛型类的真实类型的Class
     */
    private Class<T> entityClass;

    /**
     * lock key前缀
     */
    private static String Lock_Prefix = "easy:excel:upload:lock";

    /**
     * executorService
     */
    protected static ExecutorService executorService = new ThreadPoolExecutor(4, 10,
            1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(100, true));

    /**
     * AbstractService
     */
    public AbstractExcelUploadService() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericInterfaces()[0];
        entityClass = (Class<T>) pt.getActualTypeArguments()[0];
    }

    @Override
    public void execute(C context, boolean isParallel) {
        // 预处理
        List<T> list = beforeProcess(context);

        // 同步执行业务逻辑
        this.doProcess(context, list, isParallel, null);
    }

    @Override
    public void asyncExecute(C context, boolean isParallel) {
        // 预处理
        List<T> list = beforeProcess(context);

        // 异步执行业务逻辑
        executorService.execute(() -> this.doProcess(context, list, isParallel, null));
    }

    @Override
    public void asyncExecute(C context, boolean isParallel, ExecutorService executor) {

        if (executor == null) {
            throw new ServiceException("自定义线程池不能为空");
        }

        // 预处理
        List<T> list = beforeProcess(context);

        // 异步执行业务逻辑
        executor.execute(() -> this.doProcess(context, list, isParallel, executor));
    }

    /**
     * 预处理
     *
     * <p>
     *     1. context校验
     *     2. excel 转 list
     *     3. excel非空校验
     *     4. excel最大行数校验
     * </p>
     * @param context
     * @return
     */
    private List<T> beforeProcess(C context) {
        // 任务id
        String taskId = context.getTaskId();

        long start = System.currentTimeMillis();

        ExcelReader reader = null;

        InputStream inputStream = null;

        try {
            log.info("通用上传任务，taskId={}，任务开始...", taskId);

            // 校验入参
            checkContext(context);

            // 同一时间同一个用户只能有一个任务进行
            boolean b = lock(context);
            if (!b) {
                log.info("通用上传任务，taskId={}，有任务正在处理中，忽略本次任务。", taskId);
                throw new ServiceException("您当前有导入任务正在处理中，请耐心等待任务完成，稍后再试。");
            }

            // 读取Excel
            inputStream = context.getFile().getInputStream();
            reader = ExcelUtil.getReader(inputStream);

            // 指定excel标题别名
            LinkedHashMap<String, String> headerAlias = addHeaderAlias();
            if (MapUtils.isEmpty(headerAlias)) {
                log.warn("通用上传任务，taskId={}，excel标题别名不能为空。", taskId);
                throw new ServiceException("excel标题别名不能为空");
            }
            Iterator<Map.Entry<String, String>> iter = headerAlias.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                reader.addHeaderAlias(entry.getValue(), entry.getKey());
            }

            // 转实体列表
            List<T> list = this.readAll(reader, entityClass);

            log.info("通用上传任务，taskId={}，excel解析完成，总行数{}，耗时{}毫秒，总耗时{}毫秒",
                    taskId,
                    CollectionUtils.isEmpty(list) ? 0 : list.size(),
                    System.currentTimeMillis() - start,
                    System.currentTimeMillis() - start);

            // 校验
            if (CollectionUtils.isEmpty(list)) {
                log.info("通用上传任务，taskId={}，上传文件为空，任务结束，总耗时{}毫秒", taskId, System.currentTimeMillis() - start);
                throw new ServiceException("上传文件不能为空");
            }

            if (list.size() > getMaxSize()) {
                log.info("通用上传任务，taskId={}，excel超过最大行数，任务结束，总耗时{}毫秒", taskId, System.currentTimeMillis() - start);
                throw new ServiceException("上传文件最大行数不能超过" + getMaxSize() + "行");
            }

            return list;

        } catch (ValidateException ve) {
            // 异常需要释放锁
            unLock(context);
            log.error(ve.getMessage());
            throw new ServiceException(ve.getMessage());
        } catch (Exception e) {
            // 异常需要释放锁
            unLock(context);
            log.error("上传Excel失败，" + e.getMessage(), e);
            throw new ServiceException("上传Excel失败，" + e.getMessage());
        } finally {
            IoUtil.close(reader);
            IoUtil.close(inputStream);
        }
    }

    /**
     * 执行具体业务
     * @param context
     * @param list
     */
    private void doProcess(C context, List<T> list, boolean isParallel, ExecutorService executor) {
        // 任务id
        String taskId = context.getTaskId();

        long start = System.currentTimeMillis();

        try {

            // 总行数
            int totalSize = list.size();

            // 初始化任务总行数
            context.getTask().setTotal(totalSize);
            // 更新任务进度
            updateTask(context, 0, 0, null);

            // 1. 数据去重
            long validStart = System.currentTimeMillis();
            int validSize = totalSize;
            List<T> validList = this.deDuplication(deDuplicationKey(), list, context, duplicationErrorMessage());
            // 更新任务进度
            int failNums = validSize - (CollectionUtils.isEmpty(validList) ? 0 : validList.size());
            updateTask(context, failNums, 0, null);
            log.info("通用上传任务，taskId={}，完成数据去重校验，过滤出重复数据{}条，耗时{}毫秒，总耗时{}毫秒",
                    taskId,
                    failNums,
                    System.currentTimeMillis() - validStart,
                    System.currentTimeMillis() - start);


            // 2. 非空校验，格式校验等
            validStart = System.currentTimeMillis();
            validSize = validList.size();
            validList = validList.stream().filter(item -> this.validate(item, context)).collect(toList());
            // 更新任务进度
            failNums = validSize - (CollectionUtils.isEmpty(validList) ? 0 : validList.size());
            updateTask(context, failNums, 0, null);
            log.info("通用上传任务，taskId={}，完成数据非空、格式等基本校验，过滤出无效数据{}条，耗时{}毫秒，总耗时{}毫秒",
                    taskId,
                    failNums,
                    System.currentTimeMillis() - validStart,
                    System.currentTimeMillis() - start);

            if (CollectionUtils.isEmpty(validList)) {
                log.info("通用上传任务，taskId={}，excel中没有有效数据(非空、格式等基本校验)，总耗时{}毫秒", taskId, System.currentTimeMillis() - start);
                updateTask(context, 0, "excel中没有有效数据。");
                return;
            }

            // 3. 字段业务校验
            validStart = System.currentTimeMillis();
            validSize = validList.size();
            validList = filterList(validList, context);
            // 更新任务进度
            failNums = validSize - (CollectionUtils.isEmpty(validList) ? 0 : validList.size());
            updateTask(context, failNums, 0, null);

            log.info("通用上传任务，taskId={}，完成业务数据校验，过滤出无效数据{}条，耗时{}毫秒，总耗时{}毫秒",
                    taskId,
                    failNums,
                    System.currentTimeMillis() - validStart,
                    System.currentTimeMillis() - start);

            if (CollectionUtils.isEmpty(validList)) {
                log.info("通用上传任务，taskId={}，excel中没有有效数据，总耗时{}毫秒", taskId, System.currentTimeMillis() - start);
                updateTask(context, 0, "excel中没有有效数据。");
                return;
            }

            // 4. 进行业务逻辑处理
            long bizStart = System.currentTimeMillis();                              // 业务操作开始时间
            List<List<T>> partList = Lists.partition(validList, partSize());         // 按照partSize分片处理

            if (isParallel) {
                this.doParallelHandle(context, partList, start, executor);
            } else {
                this.doHandle(context, partList, start);
            }

            log.info("通用上传任务，taskId={}，完成业务数据处理，耗时{}毫秒，总耗时{}毫秒", taskId, System.currentTimeMillis() - bizStart, System.currentTimeMillis() - start);

            if (context.getTask().getSuccess() == totalSize) {
                updateTask(context, 0, "任务执行成功。");
            } else {
                updateTask(context, 0, "任务执行完成，共" + totalSize + "行数据，成功处理" + context.getTask().getSuccess() + "行。");
            }
        } catch (ValidateException ve) {
            log.error(ve.getMessage());
            interruptedTask(context, ve.getMessage());
        } catch (Exception e) {
            log.error("上传Excel失败，" + e.getMessage(), e);
            interruptedTask(context, e.getMessage());
        } finally {
            // 释放锁
            unLock(context);
            // 记录失败列表
            saveFailsList(context);
        }
    }
    
    /**
     * 并发处理业务
     * @param context
     * @param partList
     * @param start
     * @throws InterruptedException
     */
    private void doParallelHandle(C context, List<List<T>> partList, long start, ExecutorService executor) throws InterruptedException {
        long bizStart = System.currentTimeMillis();                              // 业务操作开始时间
        AtomicInteger batchNo = new AtomicInteger();                             // 自增原子类
        List<Callable<Integer>> callableList = partList.stream()                 // 多线程并发处理
                .map(part -> (Callable<Integer>)() -> handle(part, context))
                .collect(toList());
        List<Future<Integer>> futures = Optional.ofNullable(executor).orElse(executorService).invokeAll(callableList); // 交给线程池处理

        for (int i = 0; i < futures.size(); i++) {
            int success = 0;
            try {
                success = futures.get(i).get();
            } catch (Exception e) {
                log.error(String.format("通用上传任务，taskId=%s，单批次处理excel数据异常，%s", context.getTaskId(), e.getMessage()), e);
            } finally {
                log.info("通用上传任务，taskId={}，进行业务数据处理，正在处理第{}批，共{}批，耗时{}毫秒，总耗时{}毫秒",
                        context.getTaskId(),
                        batchNo.incrementAndGet(),
                        partList.size(),
                        System.currentTimeMillis() - bizStart,
                        System.currentTimeMillis() - start
                );
                // 更新任务进度
                updateTask(context, partList.get(i).size(), success, null);
            }
        }
    }

    /**
     * 非并发处理业务
     * @param context
     * @param partList
     * @param start
     */
    private void doHandle(C context, List<List<T>> partList, long start) {
        for (int i = 0; i < partList.size(); i++) {
            long batchStart = System.currentTimeMillis();
            List<T> part = partList.get(i);
            int success = 0;
            try {
                success = handle(part, context);
            } catch (Exception e) {
                log.error(String.format("通用上传任务，taskId=%s，单批次处理excel数据异常，%s", context.getTaskId(), e.getMessage()), e);
            } finally {
                log.info("通用上传任务，taskId={}，进行业务数据处理，正在处理第{}批，共{}批，耗时{}毫秒，总耗时{}毫秒",
                        context.getTaskId(),
                        i + 1,
                        partList.size(),
                        System.currentTimeMillis() - batchStart,
                        System.currentTimeMillis() - start
                );
                // 更新任务进度
                updateTask(context, part.size(), success, null);
            }
        }
    }

    /**
     * 数据非空、格式等校验
     * @param t
     * @param context
     */
    private boolean validate (T t, C context) {
        try {
            t.validate();
            return true;
        } catch (Exception e) {
            context.addFail(t, e.getMessage());
            return false;
        }
    }

    /**
     * 根据对象的指定字段进行去重，并添加错误信息到context中
     * @param column
     * @param list
     * @param context
     * @param message
     * @return
     */
    public List<T> deDuplication(Function<T, Object> column, List<T> list, C context, String message) {

        if (column == null) {
            // 不需要去重
            return list;
        }

        // 去重
        Set<String> keys = Sets.newHashSetWithExpectedSize(list.size());

        // 重复key
        Set<String> duplicationKeys = Sets.newHashSetWithExpectedSize(list.size());

        List<T> validList = list.stream().filter(item -> {
            Object v = column.apply(item);
            if (v == null || (v instanceof String && StringUtils.isBlank((String)v))) {
                return true;
            }
            String s = v.toString().trim();
            if (keys.contains(s)) {
                duplicationKeys.add(s);
                context.addFail(item, message);
                return false;
            }
            keys.add(s);
            return true;
        }).collect(toList());

        if (CollectionUtils.isEmpty(duplicationKeys)) {
            return validList;
        }

        return validList.stream().filter(item -> {
            Object v = column.apply(item);
            if (v == null || (v instanceof String && StringUtils.isBlank((String)v))) {
                return true;
            }
            String s = v.toString().trim();
            if (duplicationKeys.contains(s)) {
                context.addFail(item, message);
                return false;
            }
            return true;
        }).collect(toList());
    }

    /**
     * 校验上传上下文
     * @param context
     */
    @Override
    public void checkContext(C context) {

        // 验证操作人
        Validator.validateNotNull(context.getOperator(), "操作人operator不能为空。");

        // 校验文件
        Validator.validateNotNull(context.getFile(), "上传excel，校验context失败，上传文件不能为空。");
    }

    @Override
    public List<T> readAll(ExcelReader reader, Class<T> t) {
        return reader.readAll(t);
    }

    @Override
    public Task getTask(String taskId) {
        String str = cacheService.get(UPLOAD_EXCEL_PROGRESS + taskId);
        return StringUtils.isNotEmpty(str) ? JSONObject.parseObject(str, Task.class) : null;
    }

    @Override
    public void exportFailExcel(String taskId, HttpServletRequest request, HttpServletResponse response) {

        ExcelWriter writer = null;
        ServletOutputStream out = null;

        try {
            if (StringUtils.isEmpty(taskId)) {
                return;
            }

            // 失败列表
            List<T> failList = getFailsFromRedis(taskId);

            if (CollectionUtils.isEmpty(failList)) {
                log.info("excel上传-缓存中获取失败列表，失败列表为空，taskId={}", taskId);
                return;
            }

            writer = ExcelUtil.getBigWriter();
            writer.setOnlyAlias(true);
            // 标题别名
            LinkedHashMap<String, String> headerAlias = addHeaderAlias();
            if (MapUtils.isEmpty(headerAlias)) {
                log.error("导出失败excel失败，标题别名集合为空");
                return;
            }
            headerAlias.put("message", "错误描述");
            Iterator<Map.Entry<String, String>> iter = headerAlias.entrySet().iterator();
            int i = 0;
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                writer.setColumnWidth(i++, 22);
                writer.addHeaderAlias(entry.getKey(), entry.getValue());
            }

            // 设定excel自动换行
            writer.getHeadCellStyle().setWrapText(true);
            writer.getStyleSet().setWrapText();

            this.writeFailExcel(writer, failList);

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            String fileName = new String((failFileName() + ".xlsx").getBytes(), "iso-8859-1");
            response.setHeader("Content-Disposition","attachment;filename=" + fileName);
            out = response.getOutputStream();
            writer.flush(out, true);
        } catch (Exception e) {
            log.error("excel导出失败列表失败，" + e.getMessage(), e);
        } finally {
            IoUtil.close(writer);
            IoUtil.close(out);
        }
    }

    @Override
    public void writeFailExcel(ExcelWriter writer, List<T> failList) {
        writer.write(failList, true);
    }

    /**
     * 更新任务进度
     * @param context
     * @param nums 处理的行数
     * @param message
     */
    protected void updateTask(C context, int nums, String message) {
        updateTask(context, nums, 0, message);
    }

    /**
     * 更新任务进度
     * @param context
     * @param nums 增量处理的行数
     * @param success 增量处理成功的行数
     * @param message
     */
    protected void updateTask(C context, int nums, int success, String message) {
        Task progress = context.getTask();
        progress.addProgress(nums);
        progress.addSuccess(success);
        if (StringUtils.isNotEmpty(message)) {
            progress.setMessage(message);
        }
        cacheService.setEx(UPLOAD_EXCEL_PROGRESS + context.getTaskId(), JSON.toJSONString(progress), 1L, TimeUnit.HOURS);
    }

    /**
     * 中断任务
     * @param context
     * @param message
     */
    protected void interruptedTask(C context, String message) {
        Task progress = context.getTask();
        progress.setIsRunning(false);
        progress.setInterrupted(true);
        if (StringUtils.isNotEmpty(message)) {
            progress.setMessage(message);
        }
        cacheService.setEx(UPLOAD_EXCEL_PROGRESS + context.getTaskId(), JSON.toJSONString(progress), 1L, TimeUnit.HOURS);
    }

    /**
     * 保存错误列表到redis
     * @param context
     */
    private void saveFailsList(C context) {
        // 任务id
        String taskId = context.getTaskId();

        // 保存失败列表
        if (CollectionUtils.isNotEmpty(context.getFailList())) {
            log.info("通用上传任务，taskId={}，失败数={}", context.getTaskId(), context.getFailList().size());
            // 分片存储，避免大key
            List<String> partition = Lists.partition(context.getFailList(), 200)
                    .stream().map(JSON::toJSONString)
                    .collect(Collectors.toList());
            String key = CacheConstant.UPLOAD_EXCEL_FAIL_LIST + taskId;
            cacheService.sAdd(key, partition, CacheConstant.TEN_MIN_EXPIRE, TimeUnit.SECONDS);
        }
    }

    /**
     * 缓存中获取失败列表
     * @param taskId
     * @return
     */
    protected List<T> getFailsFromRedis(String taskId) {
        String key = CacheConstant.UPLOAD_EXCEL_FAIL_LIST + taskId;
        List<String> failList = cacheService.sMembers(key);
        if (CollectionUtils.isEmpty(failList)) {
            return null;
        }
        return failList.stream().flatMap(str -> JSON.parseArray(str, entityClass).stream())
                .collect(toList());
    }

    /**
     * 加锁
     * @param context
     */
    private boolean lock (C context) {
        try {
            String key = Lock_Prefix + context.getOperator();
            return cacheService.setNX(key, String.valueOf(System.currentTimeMillis()), 1, TimeUnit.HOURS);
        } catch (Exception e) {
            log.error("通用导出任务，获取锁异常," + e.getMessage(), e);
        }
        return false;
    }

    /**
     * 解锁
     * @param context
     */
    private void unLock (C context) {
        try {
            String key = Lock_Prefix + context.getOperator();
            cacheService.del(key);
        } catch (Exception e) {
            log.error("通用导出任务，释放锁异常," + e.getMessage(), e);
        }
    }

    @Override
    public String failFileName() {
        return "导入失败列表_" + DateUtil.format(new Date(), "yyyy_MM_dd_HH_mm_ss");
    }
}

