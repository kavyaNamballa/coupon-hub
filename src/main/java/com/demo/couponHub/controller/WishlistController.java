package com.demo.couponHub.controller;

import com.demo.couponHub.dtos.PaginatedResponse;
import com.demo.couponHub.dtos.PaginationParams;
import com.demo.couponHub.dtos.WishlistRequest;
import com.demo.couponHub.exceptions.CouponException;
import com.demo.couponHub.model.Coupon;
import com.demo.couponHub.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WishlistController {
    private final WishlistService wishlistService;

    @PostMapping("/add")
    public ResponseEntity<String> addToWishlist(@RequestBody WishlistRequest request) throws CouponException {
            return ResponseEntity.ok(wishlistService.addToWishlist(request.getUserId(), request.getCouponId()));
    }

    @PostMapping("/remove")
    public ResponseEntity<String> removeFromWishlist(@RequestBody WishlistRequest request) throws Exception {
        return ResponseEntity.ok(wishlistService.removeFromWishlist(request.getUserId(), request.getCouponId()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<PaginatedResponse<Coupon>> getUserWishlistPaginated(
            @PathVariable Long userId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "DESC") String sortDirection) {
        try {
            PaginationParams params = new PaginationParams();
            params.setPage(page);
            params.setSize(size);
            params.setSortBy(sortBy);
            params.setSortDirection(sortDirection);

            PaginatedResponse<Coupon> response = wishlistService.getUserWishlist(userId, params);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}/check/{couponId}")
    public ResponseEntity<Boolean> isInWishlist(@PathVariable Long userId, @PathVariable Long couponId) {
        try {
            boolean isInWishlist = wishlistService.isInWishlist(userId, couponId);
            return ResponseEntity.ok(isInWishlist);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long> getWishlistCount(@PathVariable Long userId) {
        try {
            Long count = wishlistService.getWishlistCount(userId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(0L);
        }
    }
}
