package org.xiaowu.behappy.screw.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.xiaowu.behappy.screw.entity.Database;

import java.util.List;

/**
 * 接受前端登录请求的参数
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private Integer id;
    private String username;
    private String password;
    private String avatarUrl;
    private String token;
    private String role;
    private Integer roleId;
    private List<Database> databases;
    private Boolean ldapFlag = Boolean.FALSE;
}
