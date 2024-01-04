package org.xiaowu.behappy.screw.util;


import jakarta.servlet.http.HttpServletRequest;

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
        return TokenUtils.getUserId(token);
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
        return TokenUtils.getUserName(token);
    }
}
