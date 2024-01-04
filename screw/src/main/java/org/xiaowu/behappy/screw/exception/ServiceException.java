package org.xiaowu.behappy.screw.exception;

import lombok.Getter;

/**
 * 自定义异常
 * @author xiaowu
 */
@Getter
public class ServiceException extends RuntimeException {
    private final String code;

    public ServiceException(String code, String msg) {
        super(msg);
        this.code = code;
    }

}
