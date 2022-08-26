package org.xiaowu.behappy.screw.common.db.util;

import lombok.experimental.UtilityClass;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 0: mysql
 * 1: clickhouse
 * @author 94391
 */
@UtilityClass
public class JDBCUtils {

    /**
     * 测试连接
     * @param databaseType
     * @param address
     * @param port
     * @param userName
     * @param password
     * @return
     */
    public boolean sqlConnTest(String databaseType, String address, Integer port, String userName, String password) {
        String driverName = null;
        String url = "";
        if ("0".equals(databaseType)) {
            //mysql
            driverName = "com.mysql.cj.jdbc.Driver";
            url = "jdbc:mysql://" + address + ":" + port + "/" + "mysql" + "?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&connectTimeout=2000";
        } else if ("1".equals(databaseType)) {
            //clickhouse
            driverName = "ru.yandex.clickhouse.ClickHouseDriver";
            url = "jdbc:clickhouse://" + address + ":" + port + "/" + "default" + "?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&socket_timeout=2000";
        }
        Connection connection = null;
        try {
            Class.forName(driverName);
            connection = DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return true;
    }

    public List<String> getAllDbNames(String databaseType, String address, Integer port, String userName, String password) {
        String driverName = null;
        String url = "";
        String sql = "";
        if ("0".equals(databaseType)) {
            //mysql
            driverName = "com.mysql.cj.jdbc.Driver";
            url = "jdbc:mysql://" + address + ":" + port + "/" + "mysql" + "?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&connectTimeout=10000";
            sql = "select SCHEMA_NAME as name from information_schema.SCHEMATA;";
        } else if ("1".equals(databaseType)) {
            //clickhouse
            driverName = "ru.yandex.clickhouse.ClickHouseDriver";
            url = "jdbc:clickhouse://" + address + ":" + port + "/" + "default" + "?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&socket_timeout=10000";
            sql = "select name from system.databases;";
        }
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<String> dsNames = new ArrayList<>();
        try {
            Class.forName(driverName);
            connection = DriverManager.getConnection(url, userName, password);
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String name = rs.getString(1);
                dsNames.add(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return dsNames;
    }

    public boolean initDatabase(String sql,String address, Integer port, String userName, String password){
        //mysql
        String driverName = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://" + address + ":" + port + "/" + "mysql" + "?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&connectTimeout=10000";
        Connection connection = null;
        Statement stmt = null;
        boolean success = false;
        try {
            Class.forName(driverName);
            connection = DriverManager.getConnection(url, userName, password);
            stmt = connection.createStatement();
            success = stmt.executeUpdate(sql) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return success;
    }
}