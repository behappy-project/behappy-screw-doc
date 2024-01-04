package org.xiaowu.behappy.screw.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author xiaowu
 * 数据库对象
 */
@Data
@TableName("sys_database")
public class Database implements Serializable {

    @Serial
    private static final long serialVersionUID = -335730508266004204L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 数据库名称
     */
    private String name;

    /**
     * 数据库图标
     */
    private String icon;

    /**
     * 数据库描述
     */
    private String description;

    /**
     * pid
     */
    private Integer pid;

    /**
     * 排序
     */
    private String sortNum;

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


    @TableField(exist = false)
    private List<Database> children;

    @TableField(exist = false)
    private String dsName;

}
