package org.xiaowu.behappy.screw.dto;

import lombok.Data;
import org.xiaowu.behappy.screw.entity.Database;

import java.util.List;

/**
 * 接受前端登录请求的参数
 */
@Data
public class UserDto {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String avatarUrl;
    private String token;
    private String role;
    private Integer roleId;
    private List<Database> databases;
}
