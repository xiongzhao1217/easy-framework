package com.easy.framework.excel.service.common;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * excel缓存服务
 * <p>
 *     功能:
 *     1. 用于缓存excel上传/下载任务执行进度;
 *     2. 用于保存excel上传时,处理失败的行的失败原因.
 *     用法:
 *     业务系统需要对该接口进行实现,如分布式使用redis缓存,单机使用本地缓存,
 *     单个业务只能在spring中有一个实现被注册,否则会出现多个bean的错误,sdk
 *     有个基于redisson默认实现,可直接使用
 * </p>
 *
 * @author xiongzhao
 * @date 2021/6/27
 */
public interface CacheService {

    /**
     * 写入数据
     * @param key
     * @param value
     * @param times
     * @param timeUnit
     * @return
     */
    void setEx(String key, String value, long times, TimeUnit timeUnit);

    /**
     * 获取缓存
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 向set集合中添加元素
     * @param key
     * @param values
     * @param times
     * @param timeUnit
     * @return
     */
    boolean sAdd(String key, List<String> values, long times, TimeUnit timeUnit);

    /**
     * 获取set集合数据
     * @param key
     * @return
     */
    List<String> sMembers(String key);

    /**
     * 不存在写入,返回true,存在不写入,返回false
     * @param key
     * @param value
     * @param times
     * @param timeUnit
     * @return
     */
    boolean setNX(String key, String value, long times, TimeUnit timeUnit);

    /**
     * 删除某个key
     * @param key
     * @return
     */
    boolean del(String key);
}
