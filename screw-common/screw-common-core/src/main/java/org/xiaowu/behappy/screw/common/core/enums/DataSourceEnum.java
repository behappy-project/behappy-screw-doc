package org.xiaowu.behappy.screw.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.xiaowu.behappy.screw.common.core.constant.CommonConstant;

/**
 * 支持的数据源类型
 * @author xiaowu
 */
@Getter
@AllArgsConstructor
public enum DataSourceEnum {
    MYSQL(CommonConstant.MYSQL),

    CLICK_HOUSE(CommonConstant.CLICK_HOUSE);

    private String datasource;

}
