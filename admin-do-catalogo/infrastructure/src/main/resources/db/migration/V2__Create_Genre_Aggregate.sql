
CREATE TABLE genres (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,
    deleted_at TIMESTAMP(6) NULL
);

CREATE TABLE genre_categories (
    genre_id VARCHAR(36) NOT NULL,
    category_id VARCHAR(36) NOT NULL,
    CONSTRAINT idx_genre_category UNIQUE (genre_id, category_id),
    CONSTRAINT fk_genre_id FOREIGN KEY (genre_id) REFERENCES genres (id) ON DELETE CASCADE,
    CONSTRAINT fk_category_id FOREIGN KEY (category_id) REFERENCES category (id) ON DELETE CASCADE
);

