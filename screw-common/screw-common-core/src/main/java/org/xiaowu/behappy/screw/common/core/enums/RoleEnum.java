package org.xiaowu.behappy.screw.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 内置系统角色
 */
@Getter
@AllArgsConstructor
public enum RoleEnum {
    ROLE_ADMIN(1), ROLE_USER(2);

    private Integer id;
}