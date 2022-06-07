package org.xiaowu.behappy.screw.dto;

import lombok.Data;
import org.xiaowu.behappy.screw.common.core.enums.DataSourceEnum;

import java.util.List;

/**
 * 执行文档请求
 * @author xiaowu
 */
@Data
public class ScrewContextLoadDto {

    /**
     * 数据源
     * @see DataSourceEnum
     */
    private DataSourceEnum dataSourceEnum;

    /**
     * 批量生产文档数据库的id
     */
    private List<Integer> ids;

}
