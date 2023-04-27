package org.xiaowu.behappy.screw.common.core.constant;

/**
 *
 * @author xiaowu
 */
public interface CommonConstant {

    String ERR_CODE = "err_code";

    String ERR_MSG = "err_msg";

    String TOKEN = "token";

    /*-----------定义数据源常量-----------*/
    String MYSQL = "mysql";

    String CLICK_HOUSE = "click_house";
    /*-----------定义数据源常量-----------*/

    /*-----------定义url常量-----------*/
    String MYSQL_URL = "jdbc:mysql://%s:%d/%s?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai";

    String CLICKHOUSE_URL = "jdbc:clickhouse://%s:%d/%s?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai";
    /*-----------定义url常量-----------*/


    /*-----------定义driver class常量-----------*/
    String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";

    String CLICKHOUSE_DRIVER = "ru.yandex.clickhouse.ClickHouseDriver";
    /*-----------定义driver class常量-----------*/

    String DEFAULT_PASS = "123456";

    String REGISTER_ENABLE = "REGISTER_ENABLE";

}
