package org.xiaowu.behappy.screw.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.xiaowu.behappy.screw.constant.CommonConstant;

import java.util.Map;

/**
 * 错误页处理
 * /resources/public/error
 * @author 94391
 */
@Component
public class ErrorPageResolver implements ErrorViewResolver {

    @Override
    public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("redirect:/error/index.html");
        Object code = request.getAttribute(CommonConstant.ERR_CODE);
        Object msg = request.getAttribute(CommonConstant.ERR_MSG);
        if (code == null || msg == null) {
            mv.addObject("status",status.value());
            mv.addObject("msg",status.getReasonPhrase());
        }else {
            mv.addObject("status",code);
            mv.addObject("msg",msg);
        }
        return mv;
    }


}
