package com.demo.couponHub.repository;

import com.demo.couponHub.model.Coupon;
import com.demo.couponHub.model.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    @Query("SELECT w FROM Wishlist w WHERE w.wishlistId = :userId")
    List<Wishlist> findByUserId(@Param("userId") Long userId);

    @Query("SELECT w FROM Wishlist w WHERE w.wishlistId = :userId AND w.couponId = :couponId")
    Optional<Wishlist> findByUserIdAndCouponId(@Param("userId") Long userId, @Param("couponId") Long couponId);

    @Query("SELECT c FROM Coupon c WHERE c.id IN (SELECT w.couponId FROM Wishlist w WHERE w.wishlistId = :userId)")
    Page<Coupon> findCouponsByUserId(@Param("userId") Long userId, Pageable pageable);
}
