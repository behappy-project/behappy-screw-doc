create database if not exists sopei_screw;
use sopei_screw;
/*建表*/
-- auto-generated definition
create table sys_database
(
    id          int auto_increment comment 'id'
        primary key,
    name        varchar(255) null comment '名称',
    icon        varchar(255) null comment '图标',
    description varchar(255) null comment '描述',
    pid         int          null comment '父级id',
    sort_num    int          null comment '排序'
)
    collate = utf8mb4_unicode_ci;

-- auto-generated definition
create table sys_dict
(
    name  varchar(255) null comment '名称',
    value varchar(255) null comment '内容',
    type  varchar(255) null comment '类型'
)
    collate = utf8mb4_unicode_ci;

-- auto-generated definition
create table sys_role
(
    id          int auto_increment comment 'id'
        primary key,
    name        varchar(50)  null comment '名称',
    description varchar(255) null comment '描述',
    flag        varchar(50)  null comment '唯一标识',
    constraint sys_role_flag_uindex
        unique (flag)
)
    collate = utf8mb4_unicode_ci;

-- auto-generated definition
create table sys_role_database
(
    role_id     int not null comment '角色id',
    database_id int not null comment '菜单id',
    primary key (role_id, database_id)
)
    comment '角色菜单关系表' collate = utf8mb4_unicode_ci;

-- auto-generated definition
create table sys_user
(
    id          int auto_increment comment 'id'
        primary key,
    username    varchar(50)                         null comment '用户名',
    password    varchar(50)                         null comment '密码',
    nickname    varchar(50)                         null comment '昵称',
    email       varchar(50)                         null comment '邮箱',
    phone       varchar(20)                         null comment '电话',
    address     varchar(255)                        null comment '地址',
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    avatar_url  varchar(255)                        null comment '头像',
    role        varchar(50)                         null comment '角色',
    role_id     int                                 null
)
    collate = utf8mb4_unicode_ci;

/*插入数据*/
INSERT INTO sopei_screw.sys_dict (name, value, type) VALUES ('user', 'el-icon-user', 'icon');
INSERT INTO sopei_screw.sys_dict (name, value, type) VALUES ('house', 'el-icon-house', 'icon');
INSERT INTO sopei_screw.sys_dict (name, value, type) VALUES ('menu', 'el-icon-menu', 'icon');
INSERT INTO sopei_screw.sys_dict (name, value, type) VALUES ('s-custom', 'el-icon-s-custom', 'icon');
INSERT INTO sopei_screw.sys_dict (name, value, type) VALUES ('s-grid', 'el-icon-s-grid', 'icon');
INSERT INTO sopei_screw.sys_dict (name, value, type) VALUES ('document', 'el-icon-document', 'icon');
INSERT INTO sopei_screw.sys_dict (name, value, type) VALUES ('coffee', 'el-icon-coffee
', 'icon');
INSERT INTO sopei_screw.sys_dict (name, value, type) VALUES ('s-marketing', 'el-icon-s-marketing', 'icon');

INSERT INTO sopei_screw.sys_role (id, name, description, flag) VALUES (1, '管理员', '管理员', 'ROLE_ADMIN');
INSERT INTO sopei_screw.sys_role (id, name, description, flag) VALUES (2, '普通角色', '普通角色', 'ROLE_USER');

INSERT INTO sopei_screw.sys_user (id, username, password, nickname, email, phone, address, create_time, avatar_url, role, role_id) VALUES (1, 'admin', 'admin', '小五123', 'admin@qq.com', '13988997788', '黑龙江', '2022-01-22 21:10:27', 'https://t7.baidu.com/it/u=4198287529,2774471735&fm=193&f=GIF', 'ROLE_ADMIN', 1);