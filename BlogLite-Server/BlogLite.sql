DROP TABLE IF EXISTS blog;
CREATE TABLE blog (
    id int NOT NULL AUTO_INCREMENT,
    title varchar(255) NOT NULL,
    content_abstract varchar(255) NOT NULL DEFAULT 'No Abstract given',
    content longtext NOT NULL,
    views int NOT NULL DEFAULT 0,
    publish_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
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

DROP TABLE IF EXISTS image_mapping;
CREATE TABLE image_mapping(
    id int NOT NULL AUTO_INCREMENT,
    source varchar(255) UNIQUE,
    md5 varchar(32) NOT NULL UNIQUE,
    folder varchar(30) NOT NULL,
    original_name varchar(255),
    type_suffix varchar(10),
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS user;
CREATE TABLE `user`(
    id int NOT NULL AUTO_INCREMENT,
    email varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL UNIQUE,
    username varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    password varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    role varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

INSERT INTO user values (1, 'wsmrxd@gmail.com', 'WhiskeyXD', '114524', 'admin');