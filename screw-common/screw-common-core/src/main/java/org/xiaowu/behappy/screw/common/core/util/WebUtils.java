package org.xiaowu.behappy.screw.common.core.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;

public class WebUtils extends org.springframework.web.util.WebUtils {

    /**
     * 判断是否ajax请求
     * spring ajax 返回含有 ResponseBody 或者 RestController注解
     * @param handlerMethod HandlerMethod
     * @return 是否ajax请求
     */
    public static boolean isAjax(HandlerMethod handlerMethod) {
        ResponseBody responseBody = handlerMethod.getMethodAnnotation(ResponseBody.class);
        if (null != responseBody) {
            return true;
        }
        // 获取类上面的Annotation，可能包含组合注解，故采用spring的工具类
        Class<?> beanType = handlerMethod.getBeanType();
        responseBody = AnnotationUtils.getAnnotation(beanType, ResponseBody.class);
        if (null != responseBody) {
            return true;
        }
        return false;
    }

    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie cookie = getCookie(request, cookieName);
        return cookie == null ? null : cookie.getValue();
    }

    public static void removeCookie(HttpServletResponse response, String cookieName) {
        setCookie(response, cookieName, null, 0);

    }

    public static void setCookie(HttpServletResponse response, String cookieName, String cookieValue,
                                 int defaultMaxAge) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(defaultMaxAge);
        response.addCookie(cookie);
    }

}