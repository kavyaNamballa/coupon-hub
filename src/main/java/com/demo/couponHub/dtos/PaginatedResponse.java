package com.demo.couponHub.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponse<T> {

    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
    private boolean isFirst;
    private boolean isLast;

    public static <T> PaginatedResponse<T> of(List<T> content, int pageNumber, int pageSize, long totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);
        PaginatedResponse<T> response = new PaginatedResponse<T>();
        response.content = content;
        response.pageNumber = pageNumber;
        response.pageSize = pageSize;
        response.totalElements = totalElements;
        response.totalPages = totalPages;
        response.hasNext = pageNumber < totalPages - 1;
        response.hasPrevious = pageNumber > 0;
        response.isFirst = pageNumber == 0;
        response.isLast = pageNumber == totalPages - 1 || totalPages == 0;
        return response;
    }
}
