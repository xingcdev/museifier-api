package com.xingcdev.museum.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class CustomPage<T> {
    private List<T> data;
    private PageInfo pageInfo;

    public CustomPage(Page<T> page) {
        this.data = page.getContent();
        this.pageInfo = new PageInfo(page.getPageable().getPageNumber() + 1,
                page.getPageable().getPageSize(), page.getTotalElements(), page.getTotalPages(), page.isLast());
    }

    public CustomPage(List<T> data, int page, int size, long totalResults,  int totalPages, boolean isLast) {
        this.data = data;
        this.pageInfo = new PageInfo(page, size, totalResults, totalPages, isLast);
    }

    @Data
    @AllArgsConstructor
    public class PageInfo {
        private int page;
        private int size;
        private long totalResults;
        private int totalPages;
        private boolean isLast;
    }
}
