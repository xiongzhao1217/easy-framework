package com.easy.framework.core.domain.http.request;

/**
 * 通用分页
 *
 * @author xiongzhao
 * @date 2020/03/03
 */
public class Page {

    /**
     * 当前页
     */
    private Integer pageNum = 1;
    /**
     * 页面大小
     */
    private Integer pageSize = 10;

    /**
     * 构造
     * @param pageNum
     * @param pageSize
     */
    public Page(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    /**
     * 构造
     * @param pageNum
     * @param pageSize
     */
    public Page(Integer pageNum, Integer pageSize, String orderBy) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.orderBy = orderBy;
    }
    

    /**
     * order by
     * <p>例如：update_time desc </p>
     */
    private String orderBy;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}

