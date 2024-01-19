DROP TABLE IF EXISTS blog;
CREATE TABLE blog (
    id int NOT NULL AUTO_INCREMENT,
    title varchar(255) NOT NULL,
    content_abstract varchar(255) NOT NULL DEFAULT 'No Abstract given',
    content longtext NOT NULL,
    views int NOT NULL DEFAULT 0,
    publish_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    preview_image varchar(255),
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS comment;
CREATE TABLE comment (
    id int NOT NULL AUTO_INCREMENT,
    identify int NOT NULL DEFAULT 0,
    nickname varchar(30) NOT NULL DEFAULT 'Unknown',
    email varchar(30) NOT NULL DEFAULT '',
    enable boolean NOT NULL DEFAULT false,
    publish_time timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    content varchar(200) NOT NULL DEFAULT '',
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS blog_tag;
CREATE TABLE blog_tag (
    id int NOT NULL AUTO_INCREMENT,
    tag_name varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL UNIQUE,
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS blog_tag_mapping;
CREATE TABLE blog_tag_mapping(
    blog_id int NOT NULL,
    tag_id int NOT NULL
) ENGINE = InnoDB;

DROP TABLE IF EXISTS blog_collection;
CREATE TABLE blog_collection(
    id int NOT NULL AUTO_INCREMENT,
    image_link varchar(255),
    collection_name varchar(50) NOT NULL UNIQUE,
    description text NOT NULL,
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS blog_collection_mapping;
CREATE TABLE blog_collection_mapping(
    blog_id int NOT NULL,
    collection_id int NOT NULL
) ENGINE = InnoDB;

DROP TABLE IF EXISTS user;
CREATE TABLE user(
    id int NOT NULL AUTO_INCREMENT,
    email varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL UNIQUE,
    username varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    password varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    role varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

INSERT INTO user values (1, 'wsmrxd@gmail.com', 'WhiskeyXD', '$2a$10$i0sTd2D/68XC/r518M0u1uXlnzGDCudYCTw5ysMoogD54xligFFf6', 'admin');