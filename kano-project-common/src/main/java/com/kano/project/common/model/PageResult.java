package com.kano.project.common.model;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {
    private static final long serialVersionUID = 1L;

    private Long page;

    private Long pageSize;

    private List<T> dataList;

    private Long totalCount;

    private Integer pageCount;

    public PageResult() {

    }

    public PageResult(long page, long pageSize, long totalCount, List<T> dataList) {
        this.page = page;
        this.pageSize = pageSize;
        this.dataList = dataList;
        this.totalCount = totalCount;
        if (this.totalCount != null && this.pageSize != null && this.pageSize != 0) {
            this.pageCount = Long.valueOf(this.totalCount / this.pageSize).intValue();
            if (this.totalCount % this.pageSize != 0) {
                this.pageCount++;
            }
        }
    }
}
