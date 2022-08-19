package org.xiaowu.behappy.screw.common.core.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取当前用户信息工具类
 * @author xiaowu
 */
public class AuthContextHolder {
    /**
     * 获取userId
     * @param request
     * @return
     */
    public static Integer getUserId(HttpServletRequest request) {
        //从header获取token
        String token = request.getHeader("token");
        //jwt从token获取userid
        Integer userId = TokenUtils.getUserId(token);
        return userId;
    }

    /**
     * 获取当前用户名称
     * @param request
     * @return
     */
    public static String getUserName(HttpServletRequest request) {
        //从header获取token
        String token = request.getHeader("token");
        //jwt从token获取userid
        String userName = TokenUtils.getUserName(token);
        return userName;
    }
}
