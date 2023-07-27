DROP TABLE IF EXISTS `blog_tag`;
CREATE TABLE `blog_tag`  (
    `id` int NOT NULL AUTO_INCREMENT,
    `tag_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL UNIQUE ,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

insert into blog_tag (tag_name) values ('无');