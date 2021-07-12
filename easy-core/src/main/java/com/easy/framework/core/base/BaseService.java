package com.easy.framework.core.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easy.framework.core.domain.http.request.Page;
import com.easy.framework.core.exception.ServiceException;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 封装mybatis plus 基础服务类
 * @param <M>
 * @param <T>
 *
 * @author xiongzhao
 * @date 2010/03/03
 */
public class BaseService<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements IService<T> {

    /**
     * 分页查询
     * @param page
     * @param queryWrapper
     * @return
     */
    @Override
    public PageInfo<T> queryPage(Page page, Wrapper<T> queryWrapper) {
        return PageHelper.startPage(page.getPageNum(), page.getPageSize())
                .setOrderBy(getOrderBy(page.getOrderBy()))
                .doSelectPageInfo(() -> list(queryWrapper));
    }

    @Override
    public PageInfo<T> queryPage(Page page) {
        return PageHelper.startPage(page.getPageNum(), page.getPageSize())
                .setOrderBy(getOrderBy(page.getOrderBy()))
                .doSelectPageInfo(this::list);
    }

    @SuppressWarnings("all")
    @Override
    public List<T> queryAllSafety(Wrapper<T> queryWrapper, int pageSize) {
        int pageNo = 0;
        int totalPage = 1;
        if (pageSize <= 0) {
            pageSize = 500;
        }
        List<T> list = null;
        while (++pageNo <= totalPage) {
            PageInfo<T> page = PageHelper.startPage(pageNo, pageSize)
                    .setOrderBy("id asc")
                    .doSelectPageInfo(() -> this.list(queryWrapper));
            if (pageNo == 1) {
                totalPage = page.getPages();
                list = Lists.newArrayListWithCapacity((int) page.getTotal());
            }
            if (page.getTotal() >= Integer.MAX_VALUE) {
                throw new ServiceException("样本数据太大，存在List溢出风险");
            }
            if (CollectionUtils.isNotEmpty(page.getList())) {
                list.addAll(page.getList());
            }
        }
        return list;
    }

    @SuppressWarnings("all")
    @Override
    public List<T> queryAllSafety(int pageSize) {
        int pageNo = 0;
        int totalPage = 1;
        if (pageSize <= 0) {
            pageSize = 500;
        }
        List<T> list = null;
        while (++pageNo <= totalPage) {
            PageInfo<T> page = PageHelper.startPage(pageNo, pageSize)
                    .setOrderBy("id asc")
                    .doSelectPageInfo(() -> this.list());
            if (pageNo == 1) {
                totalPage = page.getPages();
                list = Lists.newArrayListWithCapacity((int) page.getTotal());
            }
            if (page.getTotal() >= Integer.MAX_VALUE) {
                throw new ServiceException("样本数据太大，存在List溢出风险");
            }
            if (CollectionUtils.isNotEmpty(page.getList())) {
                list.addAll(page.getList());
            }
        }
        return list;
    }

    @Override
    public <E> List<E> queryAllSafety(int pageSize, ISelect select) {
        int pageNo = 0;
        int totalPage = 1;
        if (pageSize <= 0) {
            pageSize = 500;
        }
        List<E> list = null;
        while (++pageNo <= totalPage) {
            PageInfo<E> page = PageHelper.startPage(pageNo, pageSize)
                    .doSelectPageInfo(select);
            if (page.getTotal() >= Integer.MAX_VALUE) {
                throw new ServiceException("样本数据太大，存在List溢出风险");
            }
            if (pageNo == 1) {
                totalPage = page.getPages();
                list = Lists.newArrayListWithCapacity((int) page.getTotal());
            }
            if (CollectionUtils.isNotEmpty(page.getList())) {
                list.addAll(page.getList());
            }
        }
        return list;
    }

    /**
     * 排序
     * @param orderBy
     * @return
     */
    private String getOrderBy (String orderBy) {
        return StringUtils.isNotEmpty(orderBy) ? orderBy : "id desc";
    }
}
