package org.xiaowu.behappy.screw.util;

import lombok.experimental.UtilityClass;
import org.xiaowu.behappy.screw.enums.DataSourceEnum;

import javax.sound.midi.Soundbank;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaowu
 */
@UtilityClass
public class JDBCUtils {

    /**
     * 测试连接
     * @param databaseType:
     * @param address:
     * @param port:
     * @param userName:
     * @param password:
     * @return
     */
    public boolean sqlConnTest(DataSourceEnum databaseType, String address, Integer port, String userName, String password) {
        dbConnectionInfo dbConnectionInfo = getDbConnectionInfo(databaseType, address, port, "2000");
        Connection connection = null;
        try {
            Class.forName(dbConnectionInfo.driverName);
            connection = DriverManager.getConnection(dbConnectionInfo.url, userName, password);
        } catch (Exception e) {
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ignored) {
                }
            }
        }
        return true;
    }

    public List<String> getAllDbNames(DataSourceEnum databaseType, String address, Integer port, String userName, String password) {
        dbConnectionInfo dbConnectionInfo = getDbConnectionInfo(databaseType, address, port, "10000");
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<String> dsNames = new ArrayList<>();
        try {
            Class.forName(dbConnectionInfo.driverName);
            connection = DriverManager.getConnection(dbConnectionInfo.url, userName, password);
            stmt = connection.createStatement();
            rs = stmt.executeQuery(dbConnectionInfo.sql());
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
            } catch (SQLException ignored) {
            }
        }
        return dsNames;
    }

    private static dbConnectionInfo getDbConnectionInfo(DataSourceEnum databaseType, String address, Integer port, String connectTimeout) {
        String driverName = null;
        String url = "";
        String sql = "";
        if (DataSourceEnum.MYSQL.equals(databaseType)) {
            //mysql
            driverName = "com.mysql.cj.jdbc.Driver";
            url = "jdbc:mysql://" + address + ":" + port + "/" + "mysql" + "?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&connectTimeout="+connectTimeout;
            sql = "select SCHEMA_NAME as name from information_schema.SCHEMATA;";
        } else if (DataSourceEnum.CLICK_HOUSE.equals(databaseType)) {
            //clickhouse
            driverName = "ru.yandex.clickhouse.ClickHouseDriver";
            url = "jdbc:clickhouse://" + address + ":" + port + "/" + "default" + "?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&socket_timeout="+connectTimeout;
            sql = "select name from system.databases;";
        }
        return new dbConnectionInfo(driverName, url, sql);
    }

    private record dbConnectionInfo(String driverName, String url, String sql) {
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
            } catch (SQLException ignored) {
            }
        }
        return success;
    }
}
