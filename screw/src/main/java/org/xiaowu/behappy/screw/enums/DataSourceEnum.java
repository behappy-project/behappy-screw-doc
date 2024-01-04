package org.xiaowu.behappy.screw.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.xiaowu.behappy.screw.constant.CommonConstant;

/**
 * 支持的数据源类型
 * @author xiaowu
 */
@Getter
@AllArgsConstructor
public enum DataSourceEnum {
    /**
     * mysql
     */
    MYSQL(CommonConstant.MYSQL),

    /**
     * clickhouse
     */
    CLICK_HOUSE(CommonConstant.CLICK_HOUSE);

    private final String datasource;

}
