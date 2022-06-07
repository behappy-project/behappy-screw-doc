package org.xiaowu.behappy.screw.common.core.constant;

/**
 *
 * @author xiaowu
 */
public interface CommonConstant {

    String TOKEN = "token";

    String DICT_TYPE_ICON = "icon";

    /*-----------定义数据源常量-----------*/
    String MYSQL = "mysql";

    String CLICK_HOUSE = "click_house";
    /*-----------定义数据源常量-----------*/

    /*-----------定义url常量-----------*/
    String MYSQL_URL = "jdbc:mysql://%s:%d/%s?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai";

    String CLICKHOUSE_URL = "jdbc:clickhouse://%s:%d/%s?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai";
    /*-----------定义url常量-----------*/

}
