package org.xiaowu.behappy.screw.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 内置系统角色
 * @author XIAOWU
 */
@Getter
@AllArgsConstructor
public enum RoleEnum {
    /**
     * 管理员角色
     */
    ROLE_ADMIN(1),

    /**
     * 初始普通角色
     */
    ROLE_USER(2);

    private final Integer id;
}
