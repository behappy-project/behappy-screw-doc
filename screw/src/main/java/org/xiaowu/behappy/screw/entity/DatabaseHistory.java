package org.xiaowu.behappy.screw.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author xiaowu
 * 数据库更新历史
 */
@Data
@TableName("sys_database_history")
public class DatabaseHistory implements Serializable {

    private static final long serialVersionUID = -8707481230849814827L;

    @TableId(value = "history_id", type = IdType.AUTO)
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
    private Date updateTime;

}
