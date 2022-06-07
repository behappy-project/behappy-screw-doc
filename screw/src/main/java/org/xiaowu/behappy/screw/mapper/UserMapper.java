package org.xiaowu.behappy.screw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.xiaowu.behappy.screw.dto.UserPasswordDto;
import org.xiaowu.behappy.screw.entity.User;

/**
 * @author xiaowu
 */
public interface UserMapper extends BaseMapper<User> {

    @Update("update sys_user set password = #{newPassword} where username = #{username} and password = #{password}")
    int updatePassword(UserPasswordDto userPasswordDTO);

    Page<User> findPage(Page<User> page, @Param("username") String username, @Param("email") String email, @Param("address") String address);

}
