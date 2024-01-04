package org.xiaowu.behappy.screw.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.xiaowu.behappy.screw.enums.DataSourceEnum;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 数据源配置
 * @author xiaowu
 */
@Data
@TableName("sys_datasource")
public class Datasource implements Serializable {
    @Serial
    private static final long serialVersionUID = -1189321493167790278L;

    @TableId(type = IdType.AUTO, value = "id")
    private Integer id;

    /**
     * @see DataSourceEnum
     * 数据源类型
     */
    private String dataSource;

    /**
     * 数据源名称(唯一)
     */
    private String name;

    /**
     * 地址
     */
    private String addr;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /*------------逗号分割------------*/
    /**
     * 忽略表名
     */
    private String ignoreTableName;

    /**
     * 忽略表前缀
     */
    private String ignorePrefix;

    /**
     * 忽略表后缀
     */
    private String ignoreSuffix;
    /*------------逗号分割------------*/

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
