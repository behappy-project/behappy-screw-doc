package org.xiaowu.behappy.screw.config;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.xiaowu.behappy.screw.common.db.util.JDBCUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author xiaowu
 * sql初始化
 */
@Component
@RequiredArgsConstructor
public class Initializer implements CommandLineRunner {

    private final static String DATASTORE_BASE = "screw_doc";
    private final static String DATASTORE_HOST = "127.0.0.1";
    private final static String DATASTORE_PORT = "3306";
    private final static String DATASTORE_USER = "root";
    private final static String DATASTORE_PASS = "root";

    private final JdbcTemplate jdbcTemplate;

    private final static String INIT_DATABASE = """
            create database if not exists %s;
            """;

    private final static String[] INIT_TABLE_ARR = new String[]{
            """
 -- auto-generated definition
            create table if not exists %s.sys_database
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
""",
            """
-- auto-generated definition
            create table if not exists %s.sys_datasource
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
""",
            """
-- auto-generated definition
            create table if not exists %s.sys_role
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
""",
            """
-- auto-generated definition
            create table if not exists %s.sys_role_database
            (
                role_id     int not null comment '角色id',
                database_id int not null comment '数据库id',
                primary key (role_id, database_id)
            )
                comment '角色菜单关系表' collate = utf8mb4_unicode_ci;
""",
            """
-- auto-generated definition
            create table if not exists %s.sys_user
            (
                id          int auto_increment comment 'id'
                    primary key,
                username    varchar(50)                        not null comment '用户名',
                password    varchar(50)                        not null comment '密码',
                email       varchar(50)                        null comment '邮箱',
                phone       varchar(20)                        null comment '电话',
                address     varchar(255)                       null comment '地址',
                create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
                avatar_url  varchar(255)                       null comment '头像',
                role        varchar(50)                        null comment '角色flag',
                role_id     int                                null comment '供缓存查询',
                login_type  int      default 0                 not null comment '登录类型',
                constraint username
                    unique (username)
            );
""",
            """
create table if not exists %s.sys_database_history
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
"""
    };

    private final static String INSERT_ROLE_ADMIN_SQL = """
            INSERT INTO sys_role (id, name, description, flag) VALUES (1, '管理员', '管理员', 'ROLE_ADMIN');
            """;

    private final static String INSERT_ROLE_USER_SQL = """
            INSERT INTO sys_role (id, name, description, flag) VALUES (2, '普通角色', '普通角色', 'ROLE_USER');
            """;

    private final static String INSERT_ADMIN_SQL = """
            INSERT INTO sys_user (id, username, password, email, phone, address, create_time, avatar_url, role, role_id) VALUES (1, 'admin', '21232f297a57a5a743894a0e4a801fc3', 'admin@qq.com', '13988997788', '黑龙江', '2022-01-22 21:10:27', 'https://t7.baidu.com/it/u=4198287529,2774471735&fm=193&f=GIF', 'ROLE_ADMIN', 1);
            """;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("--------------------------------初始化sql开始---------------------------------");
        String datastoreBase = System.getenv("DATASTORE_BASE");
        String mysqlHost = System.getenv("MYSQL_HOST");
        String mysqlPort = System.getenv("MYSQL_PORT");
        String mysqlUsername = System.getenv("MYSQL_USERNAME");
        String mysqlPassword = System.getenv("MYSQL_PASSWORD");
        if (StrUtil.isBlank(datastoreBase)) {
            datastoreBase = DATASTORE_BASE;
        }
        if (StrUtil.isBlank(mysqlHost)) {
            mysqlHost = DATASTORE_HOST;
        }
        if (StrUtil.isBlank(mysqlPort)) {
            mysqlPort = DATASTORE_PORT;
        }
        if (StrUtil.isBlank(mysqlUsername)) {
            mysqlUsername = DATASTORE_USER;
        }
        if (StrUtil.isBlank(mysqlPassword)) {
            mysqlPassword = DATASTORE_PASS;
        }
        String initDatabaseSql = INIT_DATABASE.replaceAll("%s", datastoreBase);
        boolean success = JDBCUtils.initDatabase(initDatabaseSql, mysqlHost, Integer.parseInt(mysqlPort), mysqlUsername, mysqlPassword);
        if (!success) {
            System.out.println("初始化数据库失败");
            System.exit(0);
        }
        String finalDatastoreBase = datastoreBase;
        String[] initTableSql = Arrays.stream(INIT_TABLE_ARR).map(str -> str.replaceAll("%s", finalDatastoreBase)).collect(Collectors.toList()).toArray(new String[INIT_TABLE_ARR.length]);
        jdbcTemplate.batchUpdate(initTableSql);
        Integer roleAdminC = jdbcTemplate.queryForObject("select count(1) from sys_role where flag = 'ROLE_ADMIN'", Integer.class);
        Integer roleUserC = jdbcTemplate.queryForObject("select count(1) from sys_role where flag = 'ROLE_USER'", Integer.class);
        Integer adminC = jdbcTemplate.queryForObject("select count(1) from sys_user where username = 'admin'", Integer.class);
        if (roleAdminC.intValue() == 0) {
            jdbcTemplate.execute(INSERT_ROLE_ADMIN_SQL);
        }
        if (roleUserC.intValue() == 0) {
            jdbcTemplate.execute(INSERT_ROLE_USER_SQL);
        }
        if (adminC.intValue() == 0) {
            jdbcTemplate.execute(INSERT_ADMIN_SQL);
        }
        System.out.println("--------------------------------初始化sql结束---------------------------------");
    }
}
