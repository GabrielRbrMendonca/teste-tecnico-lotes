package com.empresa.lotes.dto;

import java.util.List;

public class PageResponse {

    private List<?> content;
    private long totalElements;
    private int totalPages;
    private int page;
    private int size;

    public PageResponse() {}

    public PageResponse(List<?> content, long totalElements, int totalPages, int page, int size) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.page = page;
        this.size = size;
    }

    public List<?> getContent() { return content; }
    public long getTotalElements() { return totalElements; }
    public int getTotalPages() { return totalPages; }
    public int getPage() { return page; }
    public int getSize() { return size; }
}
