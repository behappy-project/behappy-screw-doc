package org.xiaowu.behappy.screw.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("sys_role")
public class Role implements Serializable {

    private static final long serialVersionUID = -6273428754556976847L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String description;

    /**
     * 唯一标识
     */
    private String flag;

}
