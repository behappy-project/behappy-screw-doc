package org.xiaowu.behappy.screw.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author xiaowu
 * 角色关联着数据库
 */
@TableName("sys_role_database")
@Data
public class RoleDatabase implements Serializable {

    @Serial
    private static final long serialVersionUID = -5722196086455463654L;

    private Integer roleId;

    private Integer databaseId;

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
