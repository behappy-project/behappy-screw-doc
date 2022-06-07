package org.xiaowu.behappy.screw.config;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.xiaowu.behappy.screw.common.core.constant.CommonConstant;
import org.xiaowu.behappy.screw.common.core.constant.HttpStatus;
import org.xiaowu.behappy.screw.common.core.exception.ServiceException;
import org.xiaowu.behappy.screw.common.core.util.ScrewStrUtils;
import org.xiaowu.behappy.screw.entity.User;
import org.xiaowu.behappy.screw.service.RoleService;
import org.xiaowu.behappy.screw.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 认证拦截, jwt认证
 * @author xiaowu
 */
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    private static final Pattern pattern = Pattern.compile("^(/doc)(.*)");

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 先从header获取token值
        String token = request.getHeader(CommonConstant.TOKEN);
        if (StrUtil.isBlank(token)) {
            // 再尝试从param中获取
            token = request.getParameter(CommonConstant.TOKEN);
        }
        // 执行认证
        // 如果仍然是空则说明未携带token
        if (StrUtil.isBlank(token)){
            System.out.println(request.getRequestURI());
            throw new ServiceException(HttpStatus.CODE_401, "无token，请重新登录");
        }
        // 获取 token 中的 user id
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            throw new ServiceException(HttpStatus.CODE_401, "token验证失败，请重新登录");
        }
        // 根据token中的userid查询数据库
        User user = userService.getById(userId);
        if (user == null) {
            throw new ServiceException(HttpStatus.CODE_401, "用户不存在，请重新登录");
        }
        // 判断当前用户是否持有
        Matcher matcher = pattern.matcher(request.getRequestURI());
        // 如果是以/doc开头的, 则查询当前用户是否有权限访问该数据库链接
        if (matcher.matches()){
            Integer roleId = user.getRoleId();
            List<String> databases = roleService.getRoleDatabasesById(roleId);
            // /doc/mysql/sp_oe_easyepc.html
            String database = ScrewStrUtils.subStr(request.getRequestURI(), 3, "/", 1, ".html");
            if (!databases.contains(database)){
                throw new ServiceException(HttpStatus.CODE_401, "权限不足");
            }
        }
        // 用户密码加签验证 token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            jwtVerifier.verify(token); // 验证token
        } catch (JWTVerificationException e) {
            throw new ServiceException(HttpStatus.CODE_401, "token验证失败，请重新登录");
        }
        return true;
    }

}
