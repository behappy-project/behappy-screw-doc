package org.xiaowu.behappy.screw.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色关联着数据库
 */
@TableName("sys_role_database")
@Data
public class RoleDatabase implements Serializable {

    private static final long serialVersionUID = -5722196086455463654L;
    private Integer roleId;
    private Integer databaseId;

}
