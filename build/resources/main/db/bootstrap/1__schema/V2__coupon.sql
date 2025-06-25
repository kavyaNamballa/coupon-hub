DROP TABLE IF EXISTS `coupons`;
CREATE TABLE `coupons`
(
    `id` BIGINT(20)   NOT NULL AUTO_INCREMENT,
    `code` varchar(255) DEFAULT NULL,
    `coupon_type` varchar(500) NOT NULL,
    `discount_value` varchar(20) NOT NULL,
    `expiry_date` date DEFAULT NULL,
    `min_purchase_amount` int,
    `description` varchar(1000),
    `brand_name` varchar(255) NOT NULL,
    `uploaded_user_id` BIGINT(20) DEFAULT NULL,
    `used_user_id` BIGINT(20) DEFAULT NULL,
    `created_at`                timestamp  DEFAULT CURRENT_TIMESTAMP,
    `updated_at`                timestamp  DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    -- Auto-nullify when user is deleted
    CONSTRAINT fk_uploaded_user FOREIGN KEY (`uploaded_user_id`)
        REFERENCES `users`(`id`) ON DELETE SET NULL,

    CONSTRAINT fk_used_user FOREIGN KEY (`used_user_id`)
        REFERENCES `users`(`id`) ON DELETE SET NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE INDEX idx_brand_name ON coupons(brand_name);

INSERT INTO coupons (
    code, coupon_type, description, discount_value, min_purchase_amount,
    brand_name, expiry_date, uploaded_user_id, used_user_id,
    created_at, updated_at
) VALUES
      ('AMZ100', 'FLAT', 'Get flat ₹100 off on Amazon orders above ₹999', '100', 999,
       'AMAZON', '2025-12-31', NULL, NULL, NOW(), NOW()),

      ('MYNTRA20', 'PERCENTAGE', '20% off on Myntra fashion items', '20%', 500,
       'MYNTRA', '2025-11-30', NULL, NULL, NOW(), NOW()),

      ('FLIP500', 'FLAT', '₹500 instant discount on Flipkart orders above ₹2000', '500', 2000,
       'FLIPKART', '2025-10-15', NULL, NULL, NOW(), NOW()),

      ('AJIO15', 'PERCENTAGE', '15% off on all Ajio purchases above ₹1500', '15%', 1500,
       'AJIO', '2025-09-30', NULL, NULL, NOW(), NOW());
