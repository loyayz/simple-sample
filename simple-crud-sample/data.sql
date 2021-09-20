DROP TABLE IF EXISTS long_id_model;
DROP TABLE IF EXISTS string_id_model;
DROP TABLE IF EXISTS auto_id_model;
DROP TABLE IF EXISTS test_user;
DROP TABLE IF EXISTS test_user_ext;
DROP TABLE IF EXISTS test_user_account;
DROP TABLE IF EXISTS twods_model;

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

CREATE TABLE twods_model
(
    id      BIGINT(20)  NOT NULL COMMENT '主键ID',
    name    VARCHAR(50) NULL DEFAULT NULL COMMENT '姓名',
    age     INT(11)     NULL DEFAULT NULL COMMENT '年龄',
    PRIMARY KEY (id)
);

CREATE TABLE test_user
(
    id   VARCHAR(50) NOT NULL,
    name VARCHAR(50) COMMENT '姓名',
    PRIMARY KEY (`id`)
);

CREATE TABLE test_user_ext
(
    user_id VARCHAR(50) NOT NULL COMMENT '用户id',
    ext     VARCHAR(200) COMMENT '拓展信息'
);

CREATE TABLE test_user_account
(
    id               VARCHAR(50) NOT NULL,
    user_id          VARCHAR(50) NOT NULL COMMENT '用户id',
    account_name     VARCHAR(50) NOT NULL COMMENT '账号',
    account_password VARCHAR(50) NOT NULL COMMENT '密码',
    PRIMARY KEY (`id`)
);
