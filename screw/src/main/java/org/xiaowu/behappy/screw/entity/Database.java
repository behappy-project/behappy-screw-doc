package org.xiaowu.behappy.screw.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author xiaowu
 * 数据库对象
 */
@Data
@TableName("sys_database")
public class Database implements Serializable {

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


    @TableField(exist = false)
    private List<Database> children;

    @TableField(exist = false)
    private String dsName;

}
