DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`
(
    `id`                          BIGINT(20)   NOT NULL AUTO_INCREMENT,
    `first_name`                  VARCHAR(255),
    `last_name`                   VARCHAR(255),
    `email`                       VARCHAR(255) NOT NULL,
    `password`                    VARCHAR(255),
    `mobile_number`               VARCHAR(20),
    `created_by_id`               BIGINT(20) DEFAULT NULL,
    `updated_by_id`               BIGINT(20) DEFAULT NULL,
    `created_date`                timestamp  DEFAULT CURRENT_TIMESTAMP,
    `updated_date`                timestamp  DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `index_users_on_email` (`email`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;