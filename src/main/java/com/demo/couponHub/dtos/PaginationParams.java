package com.demo.couponHub.dtos;

import lombok.Data;

@Data
public class PaginationParams {

    private Integer page;
    private Integer size;
    private String sortBy;
    private String sortDirection;
}
