package com.Bibibi.entity.vo;

import java.util.ArrayList;
import java.util.List;

//封装分页查询
//totalCount：表示查询返回的总记录数。
//pageSize：表示每页应该显示多少条记录。
//pageNo：表示当前是第几页。
//pageTotal：表示根据 totalCount 和 pageSize 计算出的总页数。
//list：用于存储查询结果的数据。
public class PaginationResultVO<T> {
    private Integer totalCount;
    private Integer pageSize;
    private Integer pageNo;
    private Integer pageTotal;
    private List<T> list = new ArrayList<T>();

    public PaginationResultVO(Integer totalCount, Integer pageSize, Integer pageNo, List<T> list) {
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.list = list;
    }

    public PaginationResultVO(Integer totalCount, Integer pageSize, Integer pageTotal, Integer pageNo, List<T> list) {
        if (pageNo == 0) {
            pageNo = 1;
        }
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.pageTotal = pageTotal;
        this.pageNo = pageNo;
        this.list = list;
    }

    public PaginationResultVO(List<T> list) {
        this.list = list;
    }

    public PaginationResultVO() {
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(Integer pageTotal) {
        this.pageTotal = pageTotal;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
