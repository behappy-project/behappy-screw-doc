package org.xiaowu.behappy.screw.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * User对象
 */
@Data
@TableName("sys_user")
public class User implements Serializable {
    private static final long serialVersionUID = 600215195546603137L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String username;

    private String password;

    private String email;

    private String phone;

    private String address;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private String avatarUrl;

    private String role;

    private Integer roleId;

    /**
     * @see LoginType
     */
    private LoginType loginType;
}
