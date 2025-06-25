package com.demo.couponHub.controller;

import com.demo.couponHub.dtos.CouponSearchCriteria;
import com.demo.couponHub.dtos.CouponUploadRequest;
import com.demo.couponHub.dtos.PaginatedResponse;
import com.demo.couponHub.exceptions.CouponException;
import com.demo.couponHub.model.Coupon;
import com.demo.couponHub.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/coupons")
@CrossOrigin(origins = "http://dev.couponhub.com:9999")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CouponController {
    private final CouponService couponService;

    @GetMapping("")
    public PaginatedResponse<Coupon> findCoupons(
            @RequestParam(name = "brand", required = false) String brandName,
            @RequestParam(name = "type", required = false) String couponType,
            @RequestParam(name = "discount", required = false) String discountValue,
            @RequestParam(name = "minAmount", required = false) Integer minPurchaseAmount,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "uploadedBy", required = false) Long uploadedUserId,
            @RequestParam(name = "usedBy", required = false) Long usedUserId,
            @RequestParam(name = "expiryFrom", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date expiryDateFrom,
            @RequestParam(name = "expiryTo", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date expiryDateTo,
            @RequestParam(name = "expired", required = false) Boolean isExpired,
            @RequestParam(name = "used", required = false) Boolean isUsed,
            @RequestParam(name = "onlyActive", required = false, defaultValue = "true") Boolean onlyActive,
            @RequestParam(name = "onlyUnused", required = false, defaultValue = "true") Boolean onlyUnused,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "DESC") String sortDirection) {

        CouponSearchCriteria criteria = new CouponSearchCriteria();
        criteria.setBrandName(brandName);
        criteria.setCouponType(couponType);
        criteria.setDiscountValue(discountValue);
        criteria.setMinPurchaseAmount(minPurchaseAmount);
        criteria.setDescription(description);
        criteria.setUploadedUserId(uploadedUserId);
        criteria.setUsedUserId(usedUserId);
        criteria.setExpiryDateFrom(expiryDateFrom);
        criteria.setExpiryDateTo(expiryDateTo);
        criteria.setIsExpired(isExpired);
        criteria.setIsUsed(isUsed);
        criteria.setOnlyActive(onlyActive);
        criteria.setOnlyUnused(onlyUnused);

        return couponService.findCouponsByCriteria(criteria, page, size, sortBy, sortDirection);
    }

    @PostMapping("/upload")
    public ResponseEntity<Coupon> uploadCoupon(@RequestBody CouponUploadRequest request,
            @RequestParam Long userId) {
        try {
            Coupon uploadedCoupon = couponService.uploadCoupon(request, userId);
            return ResponseEntity.ok(uploadedCoupon);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}/uploaded")
    public PaginatedResponse<Coupon> getUserUploadedCoupons(
            @PathVariable Long userId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "DESC") String sortDirection) {
        return couponService.getUserUploadedCoupons(userId, page, size, sortBy, sortDirection);
    }

    @GetMapping("/brand/{brandName}")
    public PaginatedResponse<Coupon> getCouponsByBrand(
            @PathVariable String brandName,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "DESC") String sortDirection) {
        CouponSearchCriteria criteria = new CouponSearchCriteria();
        criteria.setBrandName(brandName);
        criteria.setOnlyActive(true);
        criteria.setOnlyUnused(true);
        return couponService.findCouponsByCriteria(criteria, page, size, sortBy, sortDirection);
    }

    @PostMapping("/use")
    public ResponseEntity<String> useCoupon(@RequestParam Long couponId, @RequestParam Long userId) throws CouponException,Exception {
        return ResponseEntity.ok(couponService.useCoupon(couponId, userId));
    }

    @GetMapping("/reveal/{couponId}/{userId}")
    public ResponseEntity<String> revealCoupon(@PathVariable Long couponId, @PathVariable Long userId) {
        try {
            String code = couponService.revealCouponCode(couponId, userId);
            return ResponseEntity.ok(code);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/daily-usage/{userId}")
    public ResponseEntity<Integer> getRemainingDailyUsage(@PathVariable Long userId) {
        try {
            int remainingUsage = couponService.getRemainingDailyUsage(userId);
            return ResponseEntity.ok(remainingUsage);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(0);
        }
    }
}
