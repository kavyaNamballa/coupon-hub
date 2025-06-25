package com.demo.couponHub.dtos;

import lombok.Data;

@Data
public class WishlistRequest {

    private Long couponId;
    private Long userId;
}
