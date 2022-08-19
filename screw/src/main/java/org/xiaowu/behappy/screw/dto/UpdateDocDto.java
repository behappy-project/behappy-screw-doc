package org.xiaowu.behappy.screw.dto;

import lombok.Data;
import org.xiaowu.behappy.screw.common.core.enums.DataSourceEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
