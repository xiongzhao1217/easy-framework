package com.easy.framework.excel.service.common;

import org.redisson.api.RedissonClient;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 基于redisson的缓存实现
 * <p>
 *     业务系统如使用redisson,需要将该bean注入到spring中
 * </p>
 * @author xiongzhao
 * @date 2021/6/27
 */
public class RedissonCacheService implements CacheService{

    /**
     * redissonClient 客户端
     */
    @Resource
    private RedissonClient redissonClient;

    @Override
    public void setEx(String key, String value, long times, TimeUnit timeUnit) {
        redissonClient.getBucket(key).set(value, times, timeUnit);
    }

    @Override
    public String get(String key) {
        Object v = redissonClient.getBucket(key).get();
        return v != null ? v.toString() : null;
    }

    @Override
    public boolean sAdd(String key, List<String> values, long times, TimeUnit timeUnit) {
        boolean b = redissonClient.getList(key).expire(times, timeUnit);
        if (!b) {
            return false;
        }
        return redissonClient.getList(key).addAll(values);
    }

    @Override
    public List<String> sMembers(String key) {
        return redissonClient.<String>getList(key).readAll();
    }

    @Override
    public boolean setNX(String key, String value, long times, TimeUnit timeUnit) {
        return redissonClient.getBucket(key).trySet(value, times, timeUnit);
    }

    @Override
    public boolean del(String key) {
        return redissonClient.getBucket(key).delete();
    }
}
