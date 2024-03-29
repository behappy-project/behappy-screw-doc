package org.xiaowu.behappy.screw.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.ContainerCriteria;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.xiaowu.behappy.screw.common.cache.constants.CacheConstants;
import org.xiaowu.behappy.screw.common.core.constant.ResStatus;
import org.xiaowu.behappy.screw.common.core.enums.RoleEnum;
import org.xiaowu.behappy.screw.common.core.exception.ServiceException;
import org.xiaowu.behappy.screw.common.core.util.TokenUtils;
import org.xiaowu.behappy.screw.dto.UserDto;
import org.xiaowu.behappy.screw.dto.UserPasswordDto;
import org.xiaowu.behappy.screw.entity.Database;
import org.xiaowu.behappy.screw.entity.LoginType;
import org.xiaowu.behappy.screw.entity.User;
import org.xiaowu.behappy.screw.mapper.RoleDatabaseMapper;
import org.xiaowu.behappy.screw.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaowu
 */
@Service
@AllArgsConstructor
public class UserService extends ServiceImpl<UserMapper, User> implements IService<User> {

    private final UserMapper userMapper;

    private final RoleDatabaseMapper roleDatabaseMapper;

    private final DatabaseService databaseService;

    private final CacheManager cacheManager;

    private final ApplicationContext applicationContext;

    private final LdapTemplate ldapTemplate;

    @CachePut(value = CacheConstants.USER_CACHE, key = "#user.id")
    public User saveUser(User user) {
        return user;
    }

    private List<User> findUserByQuery(ContainerCriteria query) {
        return ldapTemplate.search(query,
                (AttributesMapper<User>) attrs -> {
                    User ldapUser = new User();
                    ldapUser.setUsername((String) attrs.get("uid").get());
                    ldapUser.setEmail((String) attrs.get("mail").get());
                    String userPassword = new String((byte[]) attrs.get("userPassword").get());
                    ldapUser.setPassword(SecureUtil.md5(userPassword));
                    ldapUser.setAddress((String) attrs.get("postalAddress").get());
                    ldapUser.setLoginType(LoginType.LDAP);
                    ldapUser.setPhone((String) attrs.get("mobile").get());
                    return ldapUser;
                });
    }

    private boolean authenticate(String loginName, String password) {
        EqualsFilter filter = new EqualsFilter("uid", loginName);
        return ldapTemplate.authenticate("", filter.toString(), password);
    }

    public UserDto login(UserDto userDTO) {
        User dbUser = null;
        // ldap方式登录
        if (userDTO.getLdapFlag()) {
            if (authenticate(userDTO.getUsername(), userDTO.getPassword())) {
                ContainerCriteria query = LdapQueryBuilder.query()
                        .where("uid")
                        .is(userDTO.getUsername());
                List<User> userList = findUserByQuery(query);
                if (!CollectionUtils.isEmpty(userList)) {
                    // 用户密码 md5加密
                    userDTO.setPassword(SecureUtil.md5(userDTO.getPassword()));
                    // 校验数据库是否包含此user数据，如果没有则save
                    LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(User::getUsername, userDTO.getUsername());
                    User userInfo = getUserInfo(queryWrapper);
                    if (userInfo == null) {
                        dbUser = userList.get(0);
                        UserService userService = applicationContext.getBean(UserService.class);
                        // 默认普通用户角色
                        dbUser.setRole(RoleEnum.ROLE_USER.toString());
                        dbUser.setRoleId(RoleEnum.ROLE_USER.getId());
                        userService.saveOrUpdate(dbUser);
                        userService.saveUser(dbUser);
                    } else {
                        dbUser = userInfo;
                    }
                }
            }
        } else {
            // 用户密码 md5加密
            userDTO.setPassword(SecureUtil.md5(userDTO.getPassword()));
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUsername, userDTO.getUsername());
            queryWrapper.eq(User::getPassword, userDTO.getPassword());
            queryWrapper.eq(User::getLoginType, LoginType.DATABASE);
            dbUser = getUserInfo(queryWrapper);
        }
        if (dbUser != null) {
            userDTO.setPassword(null);
            // 设置token
            String token = TokenUtils.genToken(dbUser.getId(), dbUser.getUsername());
            userDTO.setToken(token);
            // ROLE_ADMIN
            String role = dbUser.getRole();
            userDTO.setRole(role);
            userDTO.setRoleId(dbUser.getRoleId());
            userDTO.setUsername(dbUser.getUsername());
            userDTO.setAvatarUrl(dbUser.getAvatarUrl());
            // 设置用户的数据库列表
            setDatabases(userDTO);
            return userDTO;
        } else {
            throw new ServiceException(ResStatus.CODE_600, "用户名或密码错误");
        }
    }

    public User register(UserDto userDTO) {
        // 用户密码 md5加密
        userDTO.setPassword(SecureUtil.md5(userDTO.getPassword()));
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, userDTO.getUsername());
        queryWrapper.eq(User::getPassword, userDTO.getPassword());
        queryWrapper.eq(User::getLoginType, LoginType.DATABASE);
        User one = getUserInfo(queryWrapper);
        if (one == null) {
            one = new User();
            BeanUtil.copyProperties(userDTO, one, true);
            // 默认普通用户角色
            one.setRole(RoleEnum.ROLE_USER.toString());
            one.setRoleId(RoleEnum.ROLE_USER.getId());
            // 把 copy完之后的用户对象存储到数据库
            save(one);
        } else {
            throw new ServiceException(ResStatus.CODE_600, "用户已存在");
        }
        return one;
    }

    public void updatePassword(UserPasswordDto userPasswordDTO) {
        // md5加密
        userPasswordDTO.setPassword(SecureUtil.md5(userPasswordDTO.getPassword()));
        userPasswordDTO.setNewPassword(SecureUtil.md5(userPasswordDTO.getNewPassword()));
        int update = userMapper.updatePassword(userPasswordDTO);
        if (update < 1) {
            throw new ServiceException(ResStatus.CODE_600, "密码错误");
        }
    }

    public Page<User> findPage(Page<User> page, String username, String email, String address) {
        return userMapper.findPage(page, username, email, address);
    }

    private User getUserInfo(LambdaQueryWrapper<User> queryWrapper) {
        User one;
        try {
            // 从数据库查询用户信息
            one = getOne(queryWrapper);
        } catch (Exception e) {
            throw new ServiceException(ResStatus.CODE_500, "系统错误");
        }
        return one;
    }

    /**
     * 赋值当前角色的菜单列表
     *
     * @param userDTO
     * @return
     */
    private void setDatabases(UserDto userDTO) {
        Integer roleId = userDTO.getRoleId();
        // 当前角色的所有菜单id集合
        List<Integer> roleDatabaseIds = roleDatabaseMapper.selectByRoleId(roleId);

        // 查出系统所有的菜单(树形)
        List<Database> databases = databaseService.findDbs("");
        // new一个最后筛选完成之后的list
        List<Database> roleDatabases = new ArrayList<>();
        // 筛选当前用户角色的菜单
        for (Database database : databases) {
            if (roleDatabaseIds.contains(database.getId())) {
                roleDatabases.add(database);
            }
            List<Database> children = database.getChildren();
            // removeIf()  移除 children 里面不在 menuIds集合中的 元素
            children.removeIf(child -> !roleDatabaseIds.contains(child.getId()));
        }
        userDTO.setDatabases(roleDatabases);
    }


    @Cacheable(value = CacheConstants.USER_CACHE, key = "#id")
    public User findByUserId(Integer id) {
        return getById(id);
    }


    public void deleteBatch(List<Integer> ids) {
        UserService userService = applicationContext.getBean(UserService.class);
        boolean success = userService.removeByIds(ids);
        if (success) {
            for (Integer id : ids) {
                cacheManager.getCache(CacheConstants.USER_CACHE).evict(id);
            }
        }
    }
}
