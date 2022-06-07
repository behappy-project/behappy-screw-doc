package org.xiaowu.behappy.screw.common.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.xiaowu.behappy.screw.common.core.constant.HttpStatus;
import org.xiaowu.behappy.screw.common.core.util.Result;

/**
 * @author 小五
 */
@Slf4j
@RestControllerAdvice(basePackages = "org.xiaowu.behappy.screw")
public class DefaultExceptionHandlerConfig {

    @ExceptionHandler(ServiceException.class)
    public Result beHappyExceptionHandler(ServiceException e) {
        log.error("DefaultExceptionHandlerConfig - beHappyExceptionHandler: {}", e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }


    @ExceptionHandler(Exception.class)
    public Result exceptionHandler(Exception e) {
        log.error("DefaultExceptionHandlerConfig - exceptionHandler: {}", e.getLocalizedMessage());
        return Result.error(HttpStatus.CODE_500,e.getMessage());
    }
}