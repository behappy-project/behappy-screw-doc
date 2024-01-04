package org.xiaowu.behappy.screw.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.xiaowu.behappy.screw.enums.LoginType;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * User对象
 */
@Data
@TableName("sys_user")
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 600215195546603137L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String username;

    private String password;

    private String email;

    private String phone;

    private String address;

    private String avatarUrl;

    @TableField(value = "role_flag")
    private String role;

    private Integer roleId;

    /**
     * @see LoginType
     */
    private LoginType loginType;

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
