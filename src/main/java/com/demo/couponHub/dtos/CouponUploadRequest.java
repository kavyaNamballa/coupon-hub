package com.demo.couponHub.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class CouponUploadRequest {

    private String code;
    private String couponType;
    private String description;
    private String discountValue;
    private Integer minPurchaseAmount;
    private String brandName;
    private Date expiryDate;
}
