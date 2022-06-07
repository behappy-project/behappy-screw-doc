package org.xiaowu.behappy.screw.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.screw.common.cache.constants.CacheConstants;
import org.xiaowu.behappy.screw.common.core.constant.HttpStatus;
import org.xiaowu.behappy.screw.common.core.enums.RoleEnum;
import org.xiaowu.behappy.screw.common.core.exception.ServiceException;
import org.xiaowu.behappy.screw.common.core.util.TokenUtils;
import org.xiaowu.behappy.screw.dto.UserDto;
import org.xiaowu.behappy.screw.dto.UserPasswordDto;
import org.xiaowu.behappy.screw.entity.Database;
import org.xiaowu.behappy.screw.entity.User;
import org.xiaowu.behappy.screw.mapper.RoleDatabaseMapper;
import org.xiaowu.behappy.screw.mapper.RoleMapper;
import org.xiaowu.behappy.screw.mapper.UserMapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author xiaowu
 */
@Service
@AllArgsConstructor
public class UserService extends ServiceImpl<UserMapper, User> implements IService<User> {

    private final UserMapper userMapper;

    private final RoleMapper roleMapper;

    private final RoleDatabaseMapper roleDatabaseMapper;

    private final DatabaseService databaseService;

    private final CacheManager cacheManager;

    @Cacheable(value = CacheConstants.USER_CACHE,key = "#id")
    @Override
    public User getById(Serializable id) {
        return super.getById(id);
    }


    @CachePut(value = CacheConstants.USER_CACHE, key="#user.id")
    public User saveUpdate(User user) {
        super.saveOrUpdate(user);
        return user;
    }


    @CacheEvict(value = CacheConstants.USER_CACHE,key = "#id")
    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    public boolean removeByIds(Collection<?> list) {
        for (Object o : list) {
            cacheManager.getCache(CacheConstants.USER_CACHE).evict(o);
        }
        return super.removeByIds(list);
    }

    public UserDto login(UserDto userDTO) {
        User one = getUserInfo(userDTO);
        if (one != null) {
            BeanUtil.copyProperties(one, userDTO, true);
            // 设置token
            String token = TokenUtils.genToken(one.getId().toString(), one.getPassword());
            userDTO.setToken(token);

            String role = one.getRole(); // ROLE_ADMIN
            userDTO.setRole(role);
            // 设置用户的数据库列表
            setDatabases(userDTO);
            return userDTO;
        } else {
            throw new ServiceException(HttpStatus.CODE_600, "用户名或密码错误");
        }
    }

    public User register(UserDto userDTO) {
        User one = getUserInfo(userDTO);
        if (one == null) {
            one = new User();
            BeanUtil.copyProperties(userDTO, one, true);
            // 默认一个普通用户的角色
            one.setRole(RoleEnum.ROLE_USER.toString());
            one.setRoleId(RoleEnum.ROLE_USER.getId());
            save(one);  // 把 copy完之后的用户对象存储到数据库
        } else {
            throw new ServiceException(HttpStatus.CODE_600, "用户已存在");
        }
        return one;
    }

    public void updatePassword(UserPasswordDto userPasswordDTO) {
        int update = userMapper.updatePassword(userPasswordDTO);
        if (update < 1) {
            throw new ServiceException(HttpStatus.CODE_600, "密码错误");
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
            one = getOne(queryWrapper); // 从数据库查询用户信息
        } catch (Exception e) {
            throw new ServiceException(HttpStatus.CODE_500, "系统错误");
        }
        return one;
    }

    /**
     * 获取当前角色的菜单列表
     * @param userDTO
     * @return
     */
    private void setDatabases(UserDto userDTO) {
        Integer roleId = roleMapper.selectByFlag(userDTO.getRole());
        // 当前角色的所有菜单id集合
        List<Integer> roleDatabaseIds = roleDatabaseMapper.selectByRoleId(roleId);

        // 查出系统所有的菜单(树形)
        List<Database> databases = databaseService.findMenus("");
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
        userDTO.setRoleId(roleId);
    }

}
