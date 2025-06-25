package com.demo.couponHub.service;

import com.demo.couponHub.dtos.CouponSearchCriteria;
import com.demo.couponHub.dtos.CouponUploadRequest;
import com.demo.couponHub.dtos.PaginatedResponse;
import com.demo.couponHub.exceptions.CouponException;
import com.demo.couponHub.mappers.CouponMapper;
import com.demo.couponHub.message.Messages;
import com.demo.couponHub.model.Coupon;
import com.demo.couponHub.repository.CouponRepository;
import com.demo.couponHub.specification.CouponSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;

    public PaginatedResponse<Coupon> findCouponsByCriteria(CouponSearchCriteria criteria,boolean hide, int page, int size, String sortBy, String sortDirection) {
        Specification<Coupon> spec = buildSpecification(criteria);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection != null ? sortDirection : "DESC"),
                sortBy != null ? sortBy : "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Coupon> couponPage = couponRepository.findAll(spec, pageable);
        if(hide) {
            hideCouponCode(couponPage.getContent());
        }

        return PaginatedResponse.of(
                couponPage.getContent(),
                couponPage.getNumber(),
                couponPage.getSize(),
                couponPage.getTotalElements()
        );
    }

    private void hideCouponCode(List<Coupon> coupons) {
        for (Coupon coupon : coupons) {
            coupon.setCode(null);
        }
    }

    public Coupon uploadCoupon(CouponUploadRequest request, Long userId) {
        Coupon coupon = couponMapper.mapRequest(request);
        coupon.setCreatedAt(new Date());
        coupon.setUpdatedAt(new Date());
        coupon.setUploadedUserId(userId);
        return couponRepository.save(coupon);
    }

    public PaginatedResponse<Coupon> getUserUploadedCoupons(Long userId, int page, int size, String sortBy, String sortDirection) {
        CouponSearchCriteria criteria = new CouponSearchCriteria();
        criteria.setUploadedUserId(userId);
        criteria.setOnlyActive(false);
        criteria.setOnlyUnused(false);
        return findCouponsByCriteria(criteria, false, page, size, sortBy, sortDirection);
    }

    private Specification<Coupon> buildSpecification(CouponSearchCriteria criteria) {
        Specification<Coupon> spec = Specification.where(null);

        if (criteria.getBrandName() != null && !criteria.getBrandName().trim().isEmpty()) {
            spec = spec.and(CouponSpecification.hasBrandName(criteria.getBrandName()));
        }

        if (criteria.getCouponType() != null && !criteria.getCouponType().trim().isEmpty()) {
            spec = spec.and(CouponSpecification.hasCouponType(criteria.getCouponType()));
        }

        if (criteria.getDiscountValue() != null && !criteria.getDiscountValue().trim().isEmpty()) {
            spec = spec.and(CouponSpecification.hasDiscountValue(criteria.getDiscountValue()));
        }

        if (criteria.getMinPurchaseAmount() != null) {
            spec = spec.and(CouponSpecification.hasMinPurchaseAmount(criteria.getMinPurchaseAmount()));
        }

        if (criteria.getDescription() != null && !criteria.getDescription().trim().isEmpty()) {
            spec = spec.and(CouponSpecification.hasDescriptionContaining(criteria.getDescription()));
        }

        // Uploaded by user filter
        if (criteria.getUploadedUserId() != null) {
            spec = spec.and(CouponSpecification.uploadedByUser(criteria.getUploadedUserId()));
        }

        // Used by user filter
        if (criteria.getUsedUserId() != null) {
            spec = spec.and(CouponSpecification.usedByUser(criteria.getUsedUserId()));
        }

        // Expiry date range filters
        if (criteria.getExpiryDateFrom() != null) {
            spec = spec.and(CouponSpecification.expiresAfter(criteria.getExpiryDateFrom()));
        }

        if (criteria.getExpiryDateTo() != null) {
            spec = spec.and(CouponSpecification.expiresBefore(criteria.getExpiryDateTo()));
        }

        // Expired status filter
        if (criteria.getIsExpired() != null) {
            if (criteria.getIsExpired()) {
                spec = spec.and(CouponSpecification.isExpired());
            } else {
                spec = spec.and(CouponSpecification.isNotExpired());
            }
        }

        // Used status filter
        if (criteria.getIsUsed() != null) {
            if (criteria.getIsUsed()) {
                spec = spec.and(CouponSpecification.isUsed());
            } else {
                spec = spec.and(CouponSpecification.isUnused());
            }
        }

        // Default filters (only active and unused coupons by default)
        if (criteria.getOnlyActive() == null || criteria.getOnlyActive()) {
            spec = spec.and(CouponSpecification.isNotExpired());
        }

        if (criteria.getOnlyUnused() == null || criteria.getOnlyUnused()) {
            spec = spec.and(CouponSpecification.isUnused());
        }

        return spec;
    }

    public Coupon useCoupon(Long couponId, Long userId) throws CouponException {
        try {
            Optional<Coupon> couponOpt = couponRepository.findById(couponId);
            if (couponOpt.isEmpty()) {
                log.error("Coupon not found with id: {}", couponId);
                throw new CouponException(String.format(Messages.COUPON_NOT_FOUND, couponId));
            }
            Coupon coupon = couponOpt.get();

            // Check if coupon is already used
            if (coupon.getUsedUserId() != null) {
                log.error("Coupon is already used: {}", couponId);
                throw new CouponException(Messages.COUPON_ALREADY_USED);
            }

            // Check if coupon is expired
            if (coupon.getExpiryDate() != null && coupon.getExpiryDate().before(new Date())) {
                log.error("Coupon is expired: {}", couponId);
                throw new CouponException(Messages.COUPON_EXPIRED);
            }

            // Check daily usage limit (10 coupons per day per user)
            int dailyUsageCount = getDailyUsageCount(userId);
            if (dailyUsageCount >= 10) {
                log.error("Daily usage limit reached for user: {}", userId);
                throw new CouponException(Messages.LIMIT_EXCEEDED);
            }

            // Mark coupon as used
            coupon.setUsedUserId(userId);
            coupon.setUpdatedAt(new Date());
            couponRepository.save(coupon);

            log.info("Coupon {} used by user {} (daily usage: {})", couponId, userId, dailyUsageCount + 1);
            return coupon;
        } catch (Exception e) {
            log.error("Error using coupon: {}", e.getMessage(), e);
            throw new CouponException(e.getMessage());
        }
    }

    private int getDailyUsageCount(Long userId) {
        try {
            java.time.LocalDate today = java.time.LocalDate.now();
            java.time.LocalDateTime startOfDay = today.atStartOfDay();
            java.time.LocalDateTime endOfDay = today.atTime(23, 59, 59);

            Date startDate = java.sql.Timestamp.valueOf(startOfDay);
            Date endDate = java.sql.Timestamp.valueOf(endOfDay);

            return couponRepository.countByUsedUserIdAndUpdatedAtBetween(userId, startDate, endDate);
        } catch (Exception e) {
            log.error("Error getting daily usage count for user {}: {}", userId, e.getMessage(), e);
            return 0;
        }
    }

    public int getRemainingDailyUsage(Long userId) {
        int usedToday = getDailyUsageCount(userId);
        return Math.max(0, 10 - usedToday);
    }

    public Long getUsageCount(Long userId) {
        try{
            return couponRepository.countByUsedUserId(userId);
        } catch (Exception e) {
            log.error("Error getting usage count for user {}: {}", userId, e.getMessage(), e);
            return 0L;
        }
    }

    public String revealCouponCode(Long couponId, Long userId) {
        try {
            Optional<Coupon> couponOpt = couponRepository.findById(couponId);
            if (couponOpt.isEmpty()) {
                log.error("Coupon not found with id: {}", couponId);
                return null;
            }
            Coupon coupon = couponOpt.get();
            if (coupon.getUsedUserId() != null) {
                log.error("Coupon is already used: {}", couponId);
                return null;
            }
            if (coupon.getExpiryDate() != null && coupon.getExpiryDate().before(new Date())) {
                log.error("Coupon is expired: {}", couponId);
                return null;
            }
            return coupon.getCode();
        } catch (Exception e) {
            log.error("Error revealing coupon code: {}", e.getMessage(), e);
            return null;
        }
    }
}
