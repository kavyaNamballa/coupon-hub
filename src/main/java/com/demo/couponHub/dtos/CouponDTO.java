package com.demo.couponHub.dtos;

import com.demo.couponHub.model.User;
import lombok.Data;

import java.util.Date;

@Data
public class CouponDTO {
    private Long id;

    private String code;
    private String couponType;
    private String description;
    private String discountValue;
    private Integer minPurchaseAmount;
    private String brandName;
    private Date expiryDate;
    private Long uploadedUserId;
    private Long usedUserId;
    private User uploadedUser;
    private User usedUser;
    private Date createdAt;
    private Date updatedAt;
}
