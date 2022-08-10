package org.xiaowu.behappy.screw.dto;

import lombok.Data;
import org.xiaowu.behappy.screw.common.core.enums.DataSourceEnum;

import java.util.List;

/**
 * 更新文档请求
 * @author xiaowu
 */
@Data
public class UpdateDocDto {

    /**
     * 数据源
     * @see DataSourceEnum
     */
    private DataSourceEnum dataSourceEnum;

    /**
     * 更新文档数据库的id
     */
    private Integer id;

    /**
     * 更新内容
     */
    private String updateDocContent;

    /**
     * 更新者
     */
    private String updateUser;

}
