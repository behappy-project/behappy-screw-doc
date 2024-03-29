package org.xiaowu.behappy.screw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.xiaowu.behappy.screw.entity.Database;

import java.util.List;

/**
 * @author xiaowu
 */
public interface DatabaseMapper extends BaseMapper<Database> {

    Page<Database> pageDatabase(Page<Database> page,
                                @Param("flag") String flag,
                                @Param("datasource") String datasource,
                                @Param("name") String name,
                                @Param("database") String database);


    List<Database> findDbsOrderBySortNum(@Param("name") String name);
}
