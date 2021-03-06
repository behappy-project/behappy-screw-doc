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
            // ??????token
            String token = TokenUtils.genToken(one.getId().toString(), one.getPassword());
            userDTO.setToken(token);

            String role = one.getRole(); // ROLE_ADMIN
            userDTO.setRole(role);
            // ??????????????????????????????
            setDatabases(userDTO);
            return userDTO;
        } else {
            throw new ServiceException(HttpStatus.CODE_600, "????????????????????????");
        }
    }

    public User register(UserDto userDTO) {
        User one = getUserInfo(userDTO);
        if (one == null) {
            one = new User();
            BeanUtil.copyProperties(userDTO, one, true);
            // ?????????????????????????????????
            one.setRole(RoleEnum.ROLE_USER.toString());
            one.setRoleId(RoleEnum.ROLE_USER.getId());
            save(one);  // ??? copy??????????????????????????????????????????
        } else {
            throw new ServiceException(HttpStatus.CODE_600, "???????????????");
        }
        return one;
    }

    public void updatePassword(UserPasswordDto userPasswordDTO) {
        int update = userMapper.updatePassword(userPasswordDTO);
        if (update < 1) {
            throw new ServiceException(HttpStatus.CODE_600, "????????????");
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
            one = getOne(queryWrapper); // ??????????????????????????????
        } catch (Exception e) {
            throw new ServiceException(HttpStatus.CODE_500, "????????????");
        }
        return one;
    }

    /**
     * ?????????????????????????????????
     * @param userDTO
     * @return
     */
    private void setDatabases(UserDto userDTO) {
        Integer roleId = roleMapper.selectByFlag(userDTO.getRole());
        // ???????????????????????????id??????
        List<Integer> roleDatabaseIds = roleDatabaseMapper.selectByRoleId(roleId);

        // ???????????????????????????(??????)
        List<Database> databases = databaseService.findMenus("");
        // new?????????????????????????????????list
        List<Database> roleDatabases = new ArrayList<>();
        // ?????????????????????????????????
        for (Database database : databases) {
            if (roleDatabaseIds.contains(database.getId())) {
                roleDatabases.add(database);
            }
            List<Database> children = database.getChildren();
            // removeIf()  ?????? children ???????????? menuIds???????????? ??????
            children.removeIf(child -> !roleDatabaseIds.contains(child.getId()));
        }
        userDTO.setDatabases(roleDatabases);
        userDTO.setRoleId(roleId);
    }

}
