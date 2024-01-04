package org.xiaowu.behappy.screw.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author xiaowu
 */
@Data
@TableName("sys_role")
public class Role implements Serializable {

    @Serial
    private static final long serialVersionUID = -6273428754556976847L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String description;

    /**
     * 唯一标识
     */
    private String flag;

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
