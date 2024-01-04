package org.xiaowu.behappy.screw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.xiaowu.behappy.screw.enums.DataSourceEnum;


/**
 * 数据库测试连接
 * @author xiaowu
 */
@Data
public class CheckConnDto {
    /**
     * 数据源类型
     */
    @NotNull
    private DataSourceEnum dataSource;

    /**
     * 数据源名称(唯一)
     */
    @NotBlank
    private String name;

    /**
     * 地址
     */
    @NotBlank
    private String addr;

    /**
     * 端口
     */
    @NotNull
    private Integer port;

    /**
     * 用户名
     */
    @NotBlank
    private String username;

    /**
     * 密码
     */
    @NotBlank
    private String password;
}
