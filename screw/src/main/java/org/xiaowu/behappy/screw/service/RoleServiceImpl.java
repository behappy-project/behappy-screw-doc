package org.xiaowu.behappy.screw.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.screw.entity.Database;
import org.xiaowu.behappy.screw.entity.Role;
import org.xiaowu.behappy.screw.entity.RoleDatabase;
import org.xiaowu.behappy.screw.mapper.RoleDatabaseMapper;
import org.xiaowu.behappy.screw.mapper.RoleMapper;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiaowu
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IService<Role> {

    private final RoleDatabaseMapper roleDatabaseMapper;

    private final DatabaseServiceImpl databaseServiceImpl;

    private final CacheManager cacheManager;

    public List<String> getRoleDatabasesById(Integer roleId) {
        // 获取database ids
        List<Integer> databaseIds = roleDatabaseMapper.selectByRoleId(roleId);
        if (CollUtil.isEmpty(databaseIds)) {
            return Collections.emptyList();
        }
        // 查出这些数据库的名称
        LambdaQueryWrapper<Database> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Database::getId,databaseIds);
        List<Database> list = databaseServiceImpl.list(lambdaQueryWrapper);
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(Database::getName).collect(Collectors.toList());
    }

    public void deleteBatch(List<Integer> list) {
        RoleServiceImpl roleServiceImpl = SpringUtil.getBean(RoleServiceImpl.class);
        boolean success = roleServiceImpl.removeByIds(list);
    }

    /**
     * 补充缓存, key:role name, val:database name
     * @param roleId
     * @param databases
     */
    @Async
    public void setRoleDatabase(Integer roleId, List<Integer> databases) {
        // 先删除当前角色id所有的绑定关系
        roleDatabaseMapper.deleteByRoleId(roleId);
        // 再把前端传过来的菜单id数组绑定到当前的这个角色id上去
        List<Integer> menuIdsCopy = CollUtil.newArrayList(databases);
        for (Integer menuId : databases) {
            Database database = databaseServiceImpl.getById(menuId);
            // 二级菜单 并且传过来的menuId数组里面没有它的父级id
            if (database.getPid() != null && !menuIdsCopy.contains(database.getPid())) {
                // 那么我们就得补上这个父级id
                RoleDatabase roleDatabase = new RoleDatabase();
                roleDatabase.setRoleId(roleId);
                roleDatabase.setDatabaseId(database.getPid());
                roleDatabaseMapper.insert(roleDatabase);
                menuIdsCopy.add(database.getPid());
            }
            RoleDatabase roleDatabase = new RoleDatabase();
            roleDatabase.setRoleId(roleId);
            roleDatabase.setDatabaseId(menuId);
            roleDatabaseMapper.insert(roleDatabase);
        }
    }

    public List<Integer> getRoleDatabase(Integer roleId) {
        return roleDatabaseMapper.selectByRoleId(roleId);
    }

    public void saveRole(Role role) {
        saveOrUpdate(role);
    }
}
