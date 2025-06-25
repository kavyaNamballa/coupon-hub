package com.demo.couponHub.specification;

import com.demo.couponHub.model.Coupon;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class CouponSpecification {

    public static Specification<Coupon> hasBrandName(String brandName) {
        return (root, query, criteriaBuilder) -> {
            if (brandName == null || brandName.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("brandName")),
                    "%" + brandName.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Coupon> hasCouponType(String couponType) {
        return (root, query, criteriaBuilder) -> {
            if (couponType == null || couponType.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("couponType"), couponType);
        };
    }

    public static Specification<Coupon> hasDiscountValue(String discountValue) {
        return (root, query, criteriaBuilder) -> {
            if (discountValue == null || discountValue.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("discountValue")),
                    "%" + discountValue.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Coupon> hasMinPurchaseAmount(Integer minPurchaseAmount) {
        return (root, query, criteriaBuilder) -> {
            if (minPurchaseAmount == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("minPurchaseAmount"), minPurchaseAmount);
        };
    }

    public static Specification<Coupon> isNotExpired() {
        return (root, query, criteriaBuilder) -> {
            Date currentDate = new Date();
            return criteriaBuilder.greaterThanOrEqualTo(root.get("expiryDate"), currentDate);
        };
    }

    public static Specification<Coupon> isExpired() {
        return (root, query, criteriaBuilder) -> {
            Date currentDate = new Date();
            return criteriaBuilder.lessThan(root.get("expiryDate"), currentDate);
        };
    }

    public static Specification<Coupon> expiresBefore(Date expiryDate) {
        return (root, query, criteriaBuilder) -> {
            if (expiryDate == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("expiryDate"), expiryDate);
        };
    }

    public static Specification<Coupon> expiresAfter(Date expiryDate) {
        return (root, query, criteriaBuilder) -> {
            if (expiryDate == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("expiryDate"), expiryDate);
        };
    }

    public static Specification<Coupon> isUnused() {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.isNull(root.get("usedUserId"));
        };
    }

    public static Specification<Coupon> isUsed() {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.isNotNull(root.get("usedUserId"));
        };
    }

    public static Specification<Coupon> uploadedByUser(Long userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("uploadedUserId"), userId);
        };
    }

    public static Specification<Coupon> usedByUser(Long userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("usedUserId"), userId);
        };
    }

    public static Specification<Coupon> hasDescriptionContaining(String description) {
        return (root, query, criteriaBuilder) -> {
            if (description == null || description.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("description")),
                    "%" + description.toLowerCase() + "%"
            );
        };
    }

}
