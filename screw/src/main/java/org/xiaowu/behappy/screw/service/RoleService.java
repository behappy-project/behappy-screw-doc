package org.xiaowu.behappy.screw.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.screw.common.cache.constants.CacheConstants;
import org.xiaowu.behappy.screw.entity.Database;
import org.xiaowu.behappy.screw.entity.Role;
import org.xiaowu.behappy.screw.entity.RoleDatabase;
import org.xiaowu.behappy.screw.mapper.RoleMapper;
import org.xiaowu.behappy.screw.mapper.RoleDatabaseMapper;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiaowu
 */
@Service
@RequiredArgsConstructor
public class RoleService extends ServiceImpl<RoleMapper, Role> implements IService<Role> {

    private final RoleDatabaseMapper roleDatabaseMapper;

    private final DatabaseService databaseService;

    private final CacheManager cacheManager;

    @Cacheable(value = CacheConstants.ROLE_CACHE,key = "#roleId")
    public List<String> getRoleDatabasesById(Integer roleId) {
        // 获取database ids
        List<Integer> databaseIds = roleDatabaseMapper.selectByRoleId(roleId);
        // 查出这些数据库的名称
        LambdaQueryWrapper<Database> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Database::getId,databaseIds);
        List<Database> list = databaseService.list(lambdaQueryWrapper);
        return list.stream().map(Database::getName).collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = CacheConstants.ROLE_CACHE,key = "#roleId")
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    public boolean removeByIds(Collection<?> list) {
        for (Object o : list) {
            cacheManager.getCache(CacheConstants.ROLE_CACHE).evict(o);
        }
        return super.removeByIds(list);
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
        // 这里存储当前角色所持有的数据库
        List<String> databaseNames = new LinkedList<>();
        for (Integer menuId : databases) {
            Database database = databaseService.getById(menuId);
            if (database.getPid() != null && !menuIdsCopy.contains(database.getPid())) { // 二级菜单 并且传过来的menuId数组里面没有它的父级id
                // 那么我们就得补上这个父级id
                RoleDatabase roleDatabase = new RoleDatabase();
                roleDatabase.setRoleId(roleId);
                roleDatabase.setDatabaseId(database.getPid());
                roleDatabaseMapper.insert(roleDatabase);
                menuIdsCopy.add(database.getPid());
            }
            databaseNames.add(database.getName());
            RoleDatabase roleDatabase = new RoleDatabase();
            roleDatabase.setRoleId(roleId);
            roleDatabase.setDatabaseId(menuId);
            roleDatabaseMapper.insert(roleDatabase);
        }
        cacheManager.getCache(CacheConstants.ROLE_CACHE).put(roleId,databaseNames);
    }

    public List<Integer> getRoleDatabase(Integer roleId) {
        return roleDatabaseMapper.selectByRoleId(roleId);
    }
}
