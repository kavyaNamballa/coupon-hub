package com.demo.couponHub.service;

import com.demo.couponHub.dtos.PaginatedResponse;
import com.demo.couponHub.dtos.PaginationParams;
import com.demo.couponHub.exceptions.CouponException;
import com.demo.couponHub.message.Messages;
import com.demo.couponHub.model.Coupon;
import com.demo.couponHub.model.Wishlist;
import com.demo.couponHub.repository.CouponRepository;
import com.demo.couponHub.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final CouponRepository couponRepository;

    public String addToWishlist(Long userId, Long couponId) {
            Optional<Coupon> couponOpt = couponRepository.findById(couponId);
            if (couponOpt.isEmpty()) {
                log.error("Coupon not found with id: {}", couponId);
                throw new CouponException(String.format(Messages.COUPON_NOT_FOUND,couponId));
            }
            Coupon coupon = couponOpt.get();
            if (coupon.getUsedUserId() != null) {
                log.error("Coupon is already used: {}", couponId);
               throw new CouponException(Messages.COUPON_ALREADY_USED);
            }
            Wishlist wishlist = new Wishlist();
            wishlist.setCouponId(couponId);
            wishlist.setWishlistId(userId);
            wishlist.setCreatedAt(new Date());
            wishlist.setUpdatedAt(new Date());

            wishlistRepository.save(wishlist);
            log.info("Added coupon {} to wishlist for user {}", couponId, userId);
            return Messages.SUCCESS;
    }

    public String removeFromWishlist(Long userId, Long couponId) throws Exception {
        try {
            Optional<Wishlist> wishlistOpt = wishlistRepository.findByUserIdAndCouponId(userId, couponId);
            if (wishlistOpt.isPresent()) {
                wishlistRepository.delete(wishlistOpt.get());
                log.info("Removed coupon {} from wishlist for user {}", couponId, userId);
                return Messages.SUCCESS;
            }
            log.info("Coupon {} not found in wishlist for user {}", couponId, userId);
            return Messages.SUCCESS;
        } catch (Exception e) {
            log.error("Error {} while removing coupon {} from wishlist for user {}",e.getMessage(), couponId, userId);
            throw e;
        }
    }

    public PaginatedResponse<Coupon> getUserWishlist(Long userId, PaginationParams paginationParams) {
        try {
            int page = paginationParams.getPage() != null ? paginationParams.getPage() : 0;
            int size = paginationParams.getSize() != null ? paginationParams.getSize() : 10;
            String sortBy = paginationParams.getSortBy() != null ? paginationParams.getSortBy() : "createdAt";
            String sortDirection = paginationParams.getSortDirection() != null ? paginationParams.getSortDirection() : "DESC";

            Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Coupon> couponPage = wishlistRepository.findCouponsByUserId(userId, pageable);

            return PaginatedResponse.of(
                    couponPage.getContent(),
                    couponPage.getNumber(),
                    couponPage.getSize(),
                    couponPage.getTotalElements()
            );
        } catch (Exception e) {
            log.error("Error fetching paginated user wishlist: {}", e.getMessage(), e);
            return PaginatedResponse.of(List.of(), 0, 10, 0);
        }
    }

    public boolean isInWishlist(Long userId, Long couponId) {
        try {
            Optional<Wishlist> wishlistOpt = wishlistRepository.findByUserIdAndCouponId(userId, couponId);
            return wishlistOpt.isPresent();
        } catch (Exception e) {
            log.error("Error checking wishlist status: {}", e.getMessage(), e);
            return false;
        }
    }

    public Long getWishlistCount(Long userId) {
        try {
            return (long) wishlistRepository.findByUserId(userId).size();
        } catch (Exception e) {
            log.error("Error getting wishlist count: {}", e.getMessage(), e);
            return 0L;
        }
    }
}
