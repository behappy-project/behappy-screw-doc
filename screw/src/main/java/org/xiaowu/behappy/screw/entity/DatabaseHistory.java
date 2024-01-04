package org.xiaowu.behappy.screw.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author xiaowu
 * 数据库更新历史
 */
@Data
@TableName("sys_database_history")
public class DatabaseHistory implements Serializable {

    @Serial
    private static final long serialVersionUID = -8707481230849814827L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 数据库id
     */
    private Integer databaseId;

    /**
     * 数据库名称
     */
    private String databaseName;

    /**
     * 更新描述
     */
    private String description;

    /**
     * 更新者
     */
    private String updateUser;

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
