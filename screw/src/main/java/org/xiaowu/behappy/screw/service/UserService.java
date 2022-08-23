package org.xiaowu.behappy.screw.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.screw.common.cache.constants.CacheConstants;
import org.xiaowu.behappy.screw.common.core.constant.ResStatus;
import org.xiaowu.behappy.screw.common.core.enums.RoleEnum;
import org.xiaowu.behappy.screw.common.core.exception.ServiceException;
import org.xiaowu.behappy.screw.common.core.util.TokenUtils;
import org.xiaowu.behappy.screw.dto.UserDto;
import org.xiaowu.behappy.screw.dto.UserPasswordDto;
import org.xiaowu.behappy.screw.entity.Database;
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

    @CachePut(value = CacheConstants.USER_CACHE, key="#user.id")
    public User saveUser(User user) {
        super.saveOrUpdate(user);
        return user;
    }


    public UserDto login(UserDto userDTO) {
        // 用户密码 md5加密
        userDTO.setPassword(SecureUtil.md5(userDTO.getPassword()));
        User dbUser = getUserInfo(userDTO);
        if (dbUser != null) {
            userDTO.setPassword(null);
            // 设置token
            String token = TokenUtils.genToken(dbUser.getId(), dbUser.getPassword());
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
        User one = getUserInfo(userDTO);
        if (one == null) {
            one = new User();
            BeanUtil.copyProperties(userDTO, one, true);
            // 默认一个普通用户的角色
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

    private User getUserInfo(UserDto userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        queryWrapper.eq("password", userDTO.getPassword());
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


    @Cacheable(value = CacheConstants.USER_CACHE,key = "#id")
    public User findByUserId(Integer id) {
        return getById(id);
    }


    public void deleteBatch(List<Integer> ids) {
        boolean success = removeByIds(ids);
        if (success) {
            for (Integer id : ids) {
                cacheManager.getCache(CacheConstants.USER_CACHE).evict(id);
            }
        }
    }
}
