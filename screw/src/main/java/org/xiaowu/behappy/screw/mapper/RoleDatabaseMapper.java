package org.xiaowu.behappy.screw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.xiaowu.behappy.screw.entity.RoleDatabase;

import java.util.List;

/**
 * @author xiaowu
 */
public interface RoleDatabaseMapper extends BaseMapper<RoleDatabase> {


    int deleteByRoleId(@Param("roleId") Integer roleId);

    List<Integer> selectByRoleId(@Param("roleId")Integer roleId);

}
