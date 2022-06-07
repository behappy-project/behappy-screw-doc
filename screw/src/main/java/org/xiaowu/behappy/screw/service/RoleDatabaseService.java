package org.xiaowu.behappy.screw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.screw.entity.RoleDatabase;
import org.xiaowu.behappy.screw.mapper.RoleDatabaseMapper;

/**
 * @author xiaowu
 */
@Service
public class RoleDatabaseService extends ServiceImpl<RoleDatabaseMapper, RoleDatabase> implements IService<RoleDatabase> {
}
