package org.xiaowu.behappy.screw.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@TableName("sys_dict")
@Data
public class Dict implements Serializable {

    private static final long serialVersionUID = 8270493400607748308L;
    private String name;
    private String value;
    private String type;

}
