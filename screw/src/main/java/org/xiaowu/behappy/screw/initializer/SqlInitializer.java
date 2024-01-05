package org.xiaowu.behappy.screw.initializer;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.xiaowu.behappy.screw.util.JDBCUtils;

import java.util.Arrays;

/**
 * @author xiaowu
 * sql初始化
 */
@Component
@RequiredArgsConstructor
public class SqlInitializer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    private final static String DATASTORE_BASE = "screw_doc";
    private final static String DATASTORE_HOST = "192.168.56.100";
    private final static String DATASTORE_PORT = "3306";
    private final static String DATASTORE_USER = "root";
    private final static String DATASTORE_PASS = "root";

    private final static String INIT_DATABASE = """
            create database if not exists %s;
            """;

    private final static String[] INIT_TABLE_ARR = new String[]{
            """
            -- auto-generated definition
            create table if not exists %s.sys_database
            (
                id          int auto_increment comment '主键',
                name        varchar(255) default '' not null comment '名称',
                icon        varchar(255) default '' not null comment '图标',
                description varchar(255) default '' not null comment '描述',
                pid         int          default 0 not null comment '父级id',
                create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
                update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间',
                sort_num    int          default 999 not null comment '排序',
                PRIMARY KEY (`id`),
                KEY `idx_pid` (`pid`)
            )
                comment '数据库信息' collate = utf8mb4_unicode_ci;
            """,
            """
            -- auto-generated definition
            create table if not exists %s.sys_datasource
            (
                id          int auto_increment comment '主键'
                    primary key,
                data_source        varchar(255) default 'MYSQL' not null comment '数据源类型',
                name        varchar(255) default '' not null comment '数据源名称(唯一)',
                addr        varchar(255) default '' not null comment '图标',
                username        varchar(255) default '' not null comment '用户名',
                password        varchar(255) default '' not null comment '密码',
                port         int          default 0 not null comment '父级id',
                create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
                update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间',
                ignore_table_name        longtext  null comment '忽略表名',
                ignore_prefix        longtext  null comment '忽略表前缀',
                ignore_suffix        longtext  null comment '忽略表后缀'
            )
                comment '数据源信息' collate = utf8mb4_unicode_ci;
            """,
            """
            -- auto-generated definition
            create table if not exists %s.sys_role
            (
                id          int auto_increment comment '主键'
                    primary key,
                name        varchar(50)  default '' not null comment '名称',
                description varchar(255) default '' not null comment '描述',
                create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
                update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间',
                flag        varchar(50)  default '' not null comment '角色唯一标识',
                constraint sys_role_flag_uindex
                    unique (flag)
            )
                comment '系统角色' collate = utf8mb4_unicode_ci;
            """,
            """
            -- auto-generated definition
            create table if not exists %s.sys_role_database
            (
                role_id     int default 0 not null comment '角色id',
                database_id int default 0 not null comment '数据库id',
                create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
                update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间',
                primary key (role_id, database_id)
            )
                comment '角色数据库关系表' collate = utf8mb4_unicode_ci;
            """,
            """
            -- auto-generated definition
            create table if not exists %s.sys_user
            (
                id          int auto_increment comment '主键'
                    primary key,
                username    varchar(50)         default ''     not null comment '用户名',
                password    varchar(50)         default ''     not null comment '密码',
                email       varchar(50)         default ''     not null comment '邮箱',
                phone       varchar(20)         default ''     not null comment '电话',
                address     varchar(255)         default ''    not null comment '地址',
                create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
                update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间',
                avatar_url  varchar(255)         default ''     not null comment '头像',
                role_flag        varchar(50)         default 'ROLE_USER'     not null comment '角色flag',
                role_id     int         default 0     not null comment '供缓存查询',
                login_type  int         default 0     not null comment '登录类型',
                constraint username
                    unique (username)
            )
                comment '系统账户' collate = utf8mb4_unicode_ci;
            """,
            """
            create table if not exists %s.sys_database_history
            (
                id int auto_increment comment '主键'
                    primary key,
                database_id int default 0 not null comment 'database_id',
                database_name varchar(256) default '' not null comment '数据库名称',
                description varchar(1000) default '' not null comment '描述',
                create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
                update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间',
                update_user varchar(256) default '' not null comment '更新者'
            )
                comment '更新历史回溯' collate = utf8mb4_unicode_ci;
            """
    };

    private final static String INSERT_ROLE_ADMIN_SQL = """
            INSERT INTO sys_role (id, name, description, flag) VALUES (1, '管理员', '管理员', 'ROLE_ADMIN');
            """;

    private final static String INSERT_ROLE_USER_SQL = """
            INSERT INTO sys_role (id, name, description, flag) VALUES (2, '普通角色', '普通角色', 'ROLE_USER');
            """;

    private final static String INSERT_ADMIN_SQL = """
            INSERT INTO sys_user (id, username, password, email, phone, address, avatar_url, role_flag, role_id) VALUES (1, 'admin', '21232f297a57a5a743894a0e4a801fc3', 'admin@example.com', '13888997788', '黑龙江', 'https://t7.baidu.com/it/u=4198287529,2774471735&fm=193&f=GIF', 'ROLE_ADMIN', 1);
            """;

    @Override
    public void run(String... args) {
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
        String[] initTableSql = Arrays.stream(INIT_TABLE_ARR)
                .map(str -> str.replaceAll("%s", finalDatastoreBase))
                .toList()
                .toArray(new String[INIT_TABLE_ARR.length]);
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
