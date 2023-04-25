package org.xiaowu.behappy.screw.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xiaowu
 */
@Getter
@AllArgsConstructor
public enum LoginType implements IEnum {

    DATABASE(0, "DATABASE"),
    LDAP(1, "LDAP");

    /**
     * 存入数据库的value值
     */
    @EnumValue
    private Integer value;

    /**
     * 返回到前端的值
     */
    @JsonValue
    private String desc;

    @Override
    public String toString() {
        return this.desc;
    }
}
