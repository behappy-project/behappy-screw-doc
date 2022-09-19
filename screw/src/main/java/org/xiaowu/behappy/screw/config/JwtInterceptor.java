package org.xiaowu.behappy.screw.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.xiaowu.behappy.screw.common.core.constant.CommonConstant;
import org.xiaowu.behappy.screw.common.core.constant.ResStatus;
import org.xiaowu.behappy.screw.common.core.exception.ServiceException;
import org.xiaowu.behappy.screw.common.core.util.ScrewStrUtils;
import org.xiaowu.behappy.screw.common.core.util.TokenUtils;
import org.xiaowu.behappy.screw.entity.User;
import org.xiaowu.behappy.screw.service.RoleService;
import org.xiaowu.behappy.screw.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.xiaowu.behappy.screw.common.core.constant.CommonConstant.ERR_CODE;
import static org.xiaowu.behappy.screw.common.core.constant.CommonConstant.ERR_MSG;

/**
 * 认证拦截, jwt认证
 * @author xiaowu
 */
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    private static final Pattern PATTERN = Pattern.compile("^/doc/.*");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean isDocFlag = false;
        PrintWriter writer = null;
        try {
            // 先从header获取token值
            String token = request.getHeader(CommonConstant.TOKEN);
            // 如果header里没有token,说明是访问文档
            if (StrUtil.isBlank(token)) {
                // 再尝试从param中获取
                token = request.getParameter(CommonConstant.TOKEN);
                isDocFlag = true;
            }
            // 执行认证
            // 如果仍然是空则说明未携带token
            if (StrUtil.isBlank(token)) {
                throw new ServiceException(ResStatus.CODE_401, "无token，请重新登录");
                //return false;
            }
            // 获取 token 中的 user id
            Integer userId = TokenUtils.getUserId(token);
            if (Objects.isNull(userId)) {
                throw new ServiceException(ResStatus.CODE_401, "token验证失败，请重新登录");
            }
            // 根据token中的userid查询数据库
            User user = userService.findByUserId(userId);
            if (user == null) {
                throw new ServiceException(ResStatus.CODE_401, "用户不存在，请重新登录");
            }
            // 判断当前用户是否持有
            Matcher matcher = PATTERN.matcher(request.getRequestURI());
            // 如果是以/doc开头的, 则查询当前用户是否有权限访问该数据库链接
            if (matcher.matches()) {
                Integer roleId = user.getRoleId();
                List<String> databases = roleService.getRoleDatabasesById(roleId);
                // /doc/${name}/xxx.html
                String database = ScrewStrUtils.subStr(request.getRequestURI(), 3, "/", 1, ".html");
                if (!databases.contains(database)){
                    throw new ServiceException(ResStatus.CODE_401, "权限不足");
                }
            }
        } catch (ServiceException e) {
            if (isDocFlag) {
                redirectIndex(request,e.getCode(),e.getMessage());
                throw new RuntimeException(e);
            }else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.set("code",e.getCode());
                jsonObject.set("msg",e.getMessage());
                response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
                response.setContentType("application/json; charset=utf-8");
                writer = response.getWriter();
                writer.append(JSONUtil.toJsonStr(jsonObject));
                writer.flush();
            }
        } catch (Exception e) {
            redirectIndex(request,ResStatus.CODE_500,e.getMessage());
            throw new RuntimeException(e);
        }finally {
            if (writer != null){
                writer.close();
            }
        }
        return true;
    }

    @SneakyThrows
    private void redirectIndex(HttpServletRequest request,String code, String msg) {
        request.setAttribute(ERR_CODE, code);
        request.setAttribute(ERR_MSG, msg);
    }


    /**
     * 获取响应状态码
     *
     * @param request
     * @return
     */
    private Integer getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        return statusCode;
    }
}
