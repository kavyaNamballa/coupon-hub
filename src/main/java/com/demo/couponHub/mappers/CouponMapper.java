package com.demo.couponHub.mappers;

import com.demo.couponHub.dtos.CouponDTO;
import com.demo.couponHub.dtos.CouponUploadRequest;
import com.demo.couponHub.model.Coupon;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CouponMapper {
    CouponDTO mapDTO(Coupon coupon);

    Coupon map(CouponDTO couponDTO);

    Coupon mapRequest(CouponUploadRequest couponUploadRequest);
}
