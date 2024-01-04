package org.xiaowu.behappy.screw.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xiaowu.behappy.screw.constant.ResStatus;

/**
 * 接口统一返回包装类
 * @author xiaowu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    private String code;
    private String msg;
    private Object data;

    public static Result success() {
        return new Result(ResStatus.CODE_200, "", null);
    }

    public static Result success(Object data) {
        return new Result(ResStatus.CODE_200, "", data);
    }

    public static Result error(String code, String msg) {
        return new Result(code, msg, null);
    }

    public static Result error() {
        return new Result(ResStatus.CODE_500, "系统错误", null);
    }

}
