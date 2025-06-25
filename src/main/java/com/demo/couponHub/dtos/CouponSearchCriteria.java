package com.demo.couponHub.dtos;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class CouponSearchCriteria {

    private String brandName;
    private String couponType;
    private String discountValue;
    private Integer minPurchaseAmount;
    private String description;
    private String code;
    private Long uploadedUserId;
    private Long usedUserId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expiryDateFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expiryDateTo;

    private Boolean isExpired;
    private Boolean isUsed;

    private Boolean onlyActive = true;
    private Boolean onlyUnused = true;

}
