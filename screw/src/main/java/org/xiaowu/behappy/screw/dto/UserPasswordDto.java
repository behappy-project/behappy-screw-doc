package org.xiaowu.behappy.screw.dto;

import lombok.Data;

/**
 * @author xiaowu
 */
@Data
public class UserPasswordDto {
    private String username;
    private String password;
    private String newPassword;
}
