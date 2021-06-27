package com.easy.framework.core.domain.http.request;

/**
 * 通用分页
 *
 * @author xiongzhao
 * @date 2020/03/03
 */
public class Page {

    /**
     * 默认构造
     */
    public Page() {}

    /**
     * 构造
     * @param pageNo
     * @param pageSize
     */
    public Page(Integer pageNo, Integer pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    /**
     * 构造
     * @param pageNo
     * @param pageSize
     */
    public Page(Integer pageNo, Integer pageSize, String orderBy) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.orderBy = orderBy;
    }

    /**
     * 当前页
     */
    private Integer pageNo = 1;
    /**
     * 页面大小
     */
    private Integer pageSize = 10;

    /**
     * order by
     * <p>例如：update_time desc </p>
     */
    private String orderBy;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
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

