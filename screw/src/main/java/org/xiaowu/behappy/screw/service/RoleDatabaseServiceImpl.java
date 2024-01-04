package org.xiaowu.behappy.screw.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.screw.entity.RoleDatabase;
import org.xiaowu.behappy.screw.mapper.RoleDatabaseMapper;

import java.util.List;

/**
 * @author xiaowu
 */
@Service
public class RoleDatabaseServiceImpl extends ServiceImpl<RoleDatabaseMapper, RoleDatabase> implements IService<RoleDatabase> {

    public boolean removeByDatabaseId(List<Integer> databaseIds) {
        LambdaQueryWrapper<RoleDatabase> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(RoleDatabase::getDatabaseId,databaseIds);
        return baseMapper.delete(lambdaQueryWrapper) > 0;
    }
}
