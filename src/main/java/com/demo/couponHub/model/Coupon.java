package com.demo.couponHub.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode()
@Entity
@Data
@Table(name = "coupons")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String couponType;
    private String description;
    private String discountValue;
    private Integer minPurchaseAmount;
    private String brandName;
    private Date expiryDate;

    @Column(name = "uploaded_user_id")
    private Long uploadedUserId;

    @Column(name = "used_user_id")
    private Long usedUserId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User uploadedUser;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "used_user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User usedUser;

    private Date createdAt;
    private Date updatedAt;

}
