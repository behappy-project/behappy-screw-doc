package org.xiaowu.behappy.screw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.xiaowu.behappy.screw.entity.Role;

/**
 * @author xiaowu
 */
public interface RoleMapper extends BaseMapper<Role> {

    @Select("select id from sys_role where flag = #{flag}")
    Integer selectByFlag(@Param("flag") String flag);

}
