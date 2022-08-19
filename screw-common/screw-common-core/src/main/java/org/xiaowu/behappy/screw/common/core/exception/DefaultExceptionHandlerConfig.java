package org.xiaowu.behappy.screw.common.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.xiaowu.behappy.screw.common.core.constant.ResStatus;
import org.xiaowu.behappy.screw.common.core.util.Result;
import org.xiaowu.behappy.screw.common.core.util.WebUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 小五
 */
@Slf4j
@RestControllerAdvice
public class DefaultExceptionHandlerConfig {

    @ExceptionHandler(ServiceException.class)
    public Result beHappyExceptionHandler(ServiceException e) {
        log.error("DefaultExceptionHandlerConfig - beHappyExceptionHandler: {}", e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }


    @ExceptionHandler(Exception.class)
    public Result exceptionHandler(Exception e) {
        log.error("DefaultExceptionHandlerConfig - exceptionHandler: {}", e.getLocalizedMessage());
        return Result.error(ResStatus.CODE_500,e.getMessage());
    }


}