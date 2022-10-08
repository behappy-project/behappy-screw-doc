package org.xiaowu.behappy.screw.dto;

import lombok.Data;
import org.xiaowu.behappy.screw.common.core.enums.DataSourceEnum;

/**
 * 根据数据源和角色查询数据库
 * @author xiaowu
 */
@Data
public class ScrewSchemaDto {

    /**
     * 数据源
     */
    private DataSourceEnum dataSource;

    /**
     * 角色
     */
    private String role;

    /**
     * 数据源名称(父级菜单)
     */
    private String name;


    /**
     * 数据库名称
     */
    private String database;

    Integer pageNum;

    Integer pageSize;

}
