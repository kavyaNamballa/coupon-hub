DROP TABLE IF EXISTS users;
CREATE TABLE users
(
    id                          BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name                  VARCHAR(255),
    last_name                   VARCHAR(255),
    email                       VARCHAR(255) NOT NULL,
    password                    VARCHAR(255),
    mobile_number               VARCHAR(20),
    created_by_id               BIGINT DEFAULT NULL,
    updated_by_id               BIGINT DEFAULT NULL,
    created_date                TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date                TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT index_users_on_email UNIQUE (email)
);