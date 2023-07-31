DROP TABLE IF EXISTS blog;
CREATE TABLE blog (
    id int NOT NULL AUTO_INCREMENT,
    title varchar(255) NOT NULL,
    content longtext NOT NULL,
    thumb_ups int NOT NULL,
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `blog_tag`;
CREATE TABLE `blog_tag`  (
    `id` int NOT NULL AUTO_INCREMENT,
    `tag_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL UNIQUE ,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS blog_tag_mapping;
CREATE TABLE blog_tag_mapping(
    blog_id int NOT NULL,
    tag_id int NOT NULL
);

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`(
    `id` int NOT NULL AUTO_INCREMENT,
    `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL UNIQUE ,
    `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `role` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

INSERT INTO user values (1, 'wsmrxd@gmail.com', 'WhiskeyXD', '114524', 'admin');