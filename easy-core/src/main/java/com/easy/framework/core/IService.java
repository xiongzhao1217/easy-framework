package com.easy.framework.core.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageInfo;
import com.jd.gms.framework.core.domain.Page;

import java.util.List;

/**
 * 封装mybatis plus 基础服务类
 * @param <T>
 *
 * @author xiongzhao
 * @date 2010/03/03
 */
public interface IService<T> extends com.baomidou.mybatisplus.extension.service.IService<T> {

    /**
     * 分页查询
     * @param page
     * @param queryWrapper
     * @return
     */
    PageInfo<T> queryPage(Page page, Wrapper<T> queryWrapper);

    /**
     * 分页查询无参数
     * @param page
     * @return
     */
    PageInfo<T> queryPage(Page page);

    /**
     * 根据条件查询全量数据
     * <p>
     *     内部采用分页查询方式实现，当预感返回数据量很大时，推荐使用该方法
     * </p>
     * @param queryWrapper
     * @return
     */
    List<T> queryAllSafety(Wrapper<T> queryWrapper, int pageSize);

    /**
     * 查询全量数据
     * <p>
     *     内部采用分页查询方式实现，当预感返回数据量很大时，推荐使用该方法
     * </p>
     * @return
     */
    List<T> queryAllSafety(int pageSize);

    /**
     * 查询全量数据
     * @param pageSize
     * @param select
     * @param <E>
     * @return
     */
    <E> List<E> queryAllSafety(int pageSize, ISelect select);
}
