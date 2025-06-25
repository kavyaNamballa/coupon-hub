package com.demo.couponHub.repository;

import com.demo.couponHub.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long>, JpaSpecificationExecutor<Coupon> {

    @Query("SELECT DISTINCT c.brandName FROM Coupon c WHERE c.brandName IS NOT NULL ORDER BY c.brandName")
    List<String> findAllBrandNames();

    int countByUsedUserIdAndUpdatedAtBetween(Long userId, Date startDate, Date endDate);

    Long countByUsedUserId(Long userId);
}
