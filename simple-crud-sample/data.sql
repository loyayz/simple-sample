DROP TABLE IF EXISTS long_id_model;
DROP TABLE IF EXISTS string_id_model;
DROP TABLE IF EXISTS auto_id_model;

CREATE TABLE long_id_model
(
    id      BIGINT(20)  NOT NULL COMMENT '主键ID',
    name    VARCHAR(50) NULL DEFAULT NULL COMMENT '姓名',
    age     INT(11)     NULL DEFAULT NULL COMMENT '年龄',
    email   VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
    role_id BIGINT(20)  NULL DEFAULT NULL COMMENT '角色ID',
    PRIMARY KEY (id)
);

CREATE TABLE string_id_model
(
    id           VARCHAR(50) NOT NULL,
    name         VARCHAR(50) COMMENT '姓名',
    gmt_create   DATETIME,
    gmt_modified DATETIME,
    PRIMARY KEY (`id`)
);

CREATE TABLE auto_id_model
(
    id   BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50),
    PRIMARY KEY (`id`)
) AUTO_INCREMENT=1;


INSERT INTO long_id_model (id, name, age, email, role_id)
VALUES (1, '张三', 10, 'test1@loyayz.com', 1),
       (2, '李四', 20, 'test2@loyayz.com', 2),
       (3, '王五', 28, 'test3@loyayz.com', 2),
       (4, '赵六', 21, 'test4@loyayz.com', 3);

INSERT INTO string_id_model (id, name, gmt_create, gmt_modified)
VALUES ('AAA', '张三', '2020-01-01 00:00:00', '2020-02-01 00:00:00'),
       ('BBB', '李四', '2020-01-02 00:00:00', '2020-02-02 00:00:00'),
       ('CCC', '王五', '2020-01-03 00:00:00', '2020-02-03 00:00:00'),
       ('DDD', '赵六', '2020-01-04 00:00:00', '2020-02-04 00:00:00');
