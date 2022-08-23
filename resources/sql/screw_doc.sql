create database if not exists screw_doc_new;
use screw_doc_new;
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
create table sys_datasource
(
    id          int auto_increment comment 'id'
        primary key,
    data_source        varchar(255) null comment '数据源类型',
    name        varchar(255) null comment '数据源名称(唯一)',
    addr        varchar(255) null comment '图标',
    username        varchar(255) null comment '用户名',
    password        varchar(255) null comment '密码',
    port         int          null comment '父级id',
    ignore_table_name        longtext null comment '忽略表名',
    ignore_prefix        longtext null comment '忽略表前缀',
    ignore_suffix        longtext null comment '忽略表后缀'
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
    database_id int not null comment '数据库id',
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
    email       varchar(50)                         null comment '邮箱',
    phone       varchar(20)                         null comment '电话',
    address     varchar(255)                        null comment '地址',
    create_time datetime default current_timestamp null comment '创建时间',
    avatar_url  varchar(255)                        null comment '头像',
    role        varchar(50)                         null comment '角色flag',
    role_id     int                                 null comment '供缓存查询'
)
    collate = utf8mb4_unicode_ci;

create table sys_database_history
(
    history_id int auto_increment
        primary key,
    database_id int not null,
    database_name varchar(256) null,
    description varchar(1000) null,
    update_time datetime default current_timestamp null,
    update_user varchar(256) null
)
    comment '更新历史回溯';


INSERT INTO sys_role (id, name, description, flag) VALUES (1, '管理员', '管理员', 'ROLE_ADMIN');
INSERT INTO sys_role (id, name, description, flag) VALUES (2, '普通角色', '普通角色', 'ROLE_USER');

INSERT INTO sys_user (id, username, password, email, phone, address, create_time, avatar_url, role, role_id) VALUES (1, 'admin', '21232f297a57a5a743894a0e4a801fc3', 'admin@qq.com', '13988997788', '黑龙江', '2022-01-22 21:10:27', 'https://t7.baidu.com/it/u=4198287529,2774471735&fm=193&f=GIF', 'ROLE_ADMIN', 1);
