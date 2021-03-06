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


    @Delete("delete from sys_role_database where role_id = #{roleId}")
    int deleteByRoleId(@Param("roleId") Integer roleId);

    @Select("select database_id from sys_role_database where role_id = #{roleId}\n" +
            "and database_id not in (select distinct pid from sys_database where pid is not null)")
    List<Integer> selectByRoleId(@Param("roleId")Integer roleId);

}
