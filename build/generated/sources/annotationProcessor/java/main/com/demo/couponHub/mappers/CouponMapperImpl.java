package com.demo.couponHub.mappers;

import com.demo.couponHub.dtos.CouponDTO;
import com.demo.couponHub.dtos.CouponUploadRequest;
import com.demo.couponHub.model.Coupon;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-26T10:53:58+0530",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.12.1.jar, environment: Java 23.0.1 (Azul Systems, Inc.)"
)
@Component
public class CouponMapperImpl implements CouponMapper {

    @Override
    public CouponDTO mapDTO(Coupon coupon) {
        if ( coupon == null ) {
            return null;
        }

        CouponDTO couponDTO = new CouponDTO();

        couponDTO.setId( coupon.getId() );
        couponDTO.setCode( coupon.getCode() );
        couponDTO.setCouponType( coupon.getCouponType() );
        couponDTO.setDescription( coupon.getDescription() );
        couponDTO.setDiscountValue( coupon.getDiscountValue() );
        couponDTO.setMinPurchaseAmount( coupon.getMinPurchaseAmount() );
        couponDTO.setBrandName( coupon.getBrandName() );
        couponDTO.setExpiryDate( coupon.getExpiryDate() );
        couponDTO.setUploadedUserId( coupon.getUploadedUserId() );
        couponDTO.setUsedUserId( coupon.getUsedUserId() );
        couponDTO.setUploadedUser( coupon.getUploadedUser() );
        couponDTO.setUsedUser( coupon.getUsedUser() );
        couponDTO.setCreatedAt( coupon.getCreatedAt() );
        couponDTO.setUpdatedAt( coupon.getUpdatedAt() );

        return couponDTO;
    }

    @Override
    public Coupon map(CouponDTO couponDTO) {
        if ( couponDTO == null ) {
            return null;
        }

        Coupon coupon = new Coupon();

        coupon.setId( couponDTO.getId() );
        coupon.setCode( couponDTO.getCode() );
        coupon.setCouponType( couponDTO.getCouponType() );
        coupon.setDescription( couponDTO.getDescription() );
        coupon.setDiscountValue( couponDTO.getDiscountValue() );
        coupon.setMinPurchaseAmount( couponDTO.getMinPurchaseAmount() );
        coupon.setBrandName( couponDTO.getBrandName() );
        coupon.setExpiryDate( couponDTO.getExpiryDate() );
        coupon.setUploadedUserId( couponDTO.getUploadedUserId() );
        coupon.setUsedUserId( couponDTO.getUsedUserId() );
        coupon.setUploadedUser( couponDTO.getUploadedUser() );
        coupon.setUsedUser( couponDTO.getUsedUser() );
        coupon.setCreatedAt( couponDTO.getCreatedAt() );
        coupon.setUpdatedAt( couponDTO.getUpdatedAt() );

        return coupon;
    }

    @Override
    public Coupon mapRequest(CouponUploadRequest couponUploadRequest) {
        if ( couponUploadRequest == null ) {
            return null;
        }

        Coupon coupon = new Coupon();

        coupon.setCode( couponUploadRequest.getCode() );
        coupon.setCouponType( couponUploadRequest.getCouponType() );
        coupon.setDescription( couponUploadRequest.getDescription() );
        coupon.setDiscountValue( couponUploadRequest.getDiscountValue() );
        coupon.setMinPurchaseAmount( couponUploadRequest.getMinPurchaseAmount() );
        coupon.setBrandName( couponUploadRequest.getBrandName() );
        coupon.setExpiryDate( couponUploadRequest.getExpiryDate() );
        coupon.setUploadedUserId( couponUploadRequest.getUploadedUserId() );
        coupon.setUsedUserId( couponUploadRequest.getUsedUserId() );

        return coupon;
    }
}
