CREATE TABLE category (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    active      BOOLEAN NOT NULL DEFAULT TRUE,
    UNIQUE KEY uq_category_name (name)
);

CREATE TABLE user (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    university_email    VARCHAR(190) NOT NULL,
    password_hash       VARCHAR(100) NOT NULL,
    full_name           VARCHAR(120) NOT NULL,
    role                VARCHAR(30)  NOT NULL,
    email_verified      BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
                                      ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uq_user_email (university_email)
);

CREATE TABLE listing (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    seller_id    BIGINT NOT NULL,
    category_id  BIGINT NOT NULL,
    title        VARCHAR(160) NOT NULL,
    description  TEXT NOT NULL,
    price        DECIMAL(12,2) NOT NULL,
    status       VARCHAR(30) NOT NULL,
    version      BIGINT NOT NULL DEFAULT 0,
    created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                             ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_listing_seller   FOREIGN KEY (seller_id)   REFERENCES user(id),
    CONSTRAINT fk_listing_category FOREIGN KEY (category_id) REFERENCES category(id),
    CONSTRAINT chk_listing_price CHECK (price >= 0),
    INDEX idx_listing_status_cat_created (status, category_id, created_at)
);

CREATE TABLE listing_image (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    listing_id  BIGINT NOT NULL,
    image_url   VARCHAR(500) NOT NULL,
    sort_order  INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_image_listing FOREIGN KEY (listing_id) REFERENCES listing(id)
        ON DELETE CASCADE
);

CREATE TABLE conversation (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    listing_id  BIGINT NOT NULL,
    buyer_id    BIGINT NOT NULL,
    seller_id   BIGINT NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_conv_listing FOREIGN KEY (listing_id) REFERENCES listing(id),
    CONSTRAINT fk_conv_buyer   FOREIGN KEY (buyer_id)   REFERENCES user(id),
    CONSTRAINT fk_conv_seller  FOREIGN KEY (seller_id)  REFERENCES user(id),
    UNIQUE KEY uq_conv_listing_buyer_seller (listing_id, buyer_id, seller_id)
);

CREATE TABLE message (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    conversation_id  BIGINT NOT NULL,
    sender_id        BIGINT NOT NULL,
    message_text     TEXT NOT NULL,
    created_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    read_at          TIMESTAMP NULL,
    CONSTRAINT fk_message_conv   FOREIGN KEY (conversation_id) REFERENCES conversation(id),
    CONSTRAINT fk_message_sender FOREIGN KEY (sender_id)       REFERENCES user(id),
    INDEX idx_message_conv_created (conversation_id, created_at)
);

CREATE TABLE `order` (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    listing_id      BIGINT NOT NULL,
    buyer_id        BIGINT NOT NULL,
    total_amount    DECIMAL(12,2) NOT NULL,
    status          VARCHAR(30) NOT NULL,
    payment_method  VARCHAR(50),
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                                ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_order_listing FOREIGN KEY (listing_id) REFERENCES listing(id),
    CONSTRAINT fk_order_buyer   FOREIGN KEY (buyer_id)   REFERENCES user(id)
);

CREATE TABLE payment (
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id             BIGINT NOT NULL,
    amount               DECIMAL(12,2) NOT NULL,
    status               VARCHAR(30) NOT NULL,
    provider_reference   VARCHAR(255) NOT NULL,
    idempotency_key      VARCHAR(255) NOT NULL,
    paid_at              TIMESTAMP NULL,
    CONSTRAINT fk_payment_order FOREIGN KEY (order_id) REFERENCES `order`(id),
    UNIQUE KEY uq_payment_order (order_id),
    UNIQUE KEY uq_payment_provider_ref (provider_reference),
    UNIQUE KEY uq_payment_idempotency (idempotency_key)
);

CREATE TABLE review (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id      BIGINT NOT NULL,
    reviewer_id   BIGINT NOT NULL,
    reviewee_id   BIGINT NOT NULL,
    rating        TINYINT NOT NULL,
    comment       TEXT,
    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                              ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_review_order    FOREIGN KEY (order_id)    REFERENCES `order`(id),
    CONSTRAINT fk_review_reviewer FOREIGN KEY (reviewer_id) REFERENCES user(id),
    CONSTRAINT fk_review_reviewee FOREIGN KEY (reviewee_id) REFERENCES user(id),
    UNIQUE KEY uq_review_order (order_id),
    CONSTRAINT chk_review_rating CHECK (rating BETWEEN 1 AND 5),
    INDEX idx_review_reviewee_created (reviewee_id, created_at)
);

CREATE TABLE notification (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    type        VARCHAR(50) NOT NULL,
    title       VARCHAR(200) NOT NULL,
    body        TEXT,
    is_read     BOOLEAN NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notification_user FOREIGN KEY (user_id) REFERENCES user(id),
    INDEX idx_notification_user_read_created (user_id, is_read, created_at)
);
