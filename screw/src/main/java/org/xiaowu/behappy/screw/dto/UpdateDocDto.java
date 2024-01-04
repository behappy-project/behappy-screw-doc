package org.xiaowu.behappy.screw.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.xiaowu.behappy.screw.enums.DataSourceEnum;


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
    @NotNull
    private DataSourceEnum dataSourceEnum;

    /**
     * 更新文档数据库的id
     */
    @NotNull
    private Integer id;

    /**
     * 更新内容
     */
    private String updateDocContent;

}
