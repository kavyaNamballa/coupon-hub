DROP TABLE IF EXISTS `wishlist`;
CREATE TABLE `wishlist`
(
    `id`       BIGINT(20)   NOT NULL AUTO_INCREMENT,
    `coupon_id`   BIGINT(20)   NOT NULL,
    `wishlist_id`  BIGINT(20)   NOT NULL,
    `created_at`                timestamp  DEFAULT CURRENT_TIMESTAMP,
    `updated_at`                timestamp  DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    FOREIGN KEY (coupon_id) REFERENCES coupons(id),
    FOREIGN KEY (wishlist_id) REFERENCES users(id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;