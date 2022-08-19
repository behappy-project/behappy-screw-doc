package org.xiaowu.behappy.screw.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.xiaowu.behappy.screw.common.core.enums.DataSourceEnum;

import java.io.Serializable;

/**
 * 数据源配置
 * @author xiaowu
 */
@Data
@TableName("sys_datasource")
public class Datasource implements Serializable {
    private static final long serialVersionUID = -1189321493167790278L;

    @TableId(type = IdType.AUTO,value = "id")
    private Integer id;

    /**
     * 数据源类型
     */
    private DataSourceEnum dataSource;

    /**
     * 数据源名称(唯一)
     */
    private String name;

    /**
     * 地址
     */
    private String addr;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /*------------逗号分割------------*/
    /**
     * 忽略表名
     */
    private String ignoreTableName;

    /**
     * 忽略表前缀
     */
    private String ignorePrefix;

    /**
     * 忽略表后缀
     */
    private String ignoreSuffix;
    /*------------逗号分割------------*/
}
