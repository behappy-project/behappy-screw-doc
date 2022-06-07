package org.xiaowu.behappy.screw.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.screw.common.core.config.ScrewProperties;
import org.xiaowu.behappy.screw.dto.ScrewSchemaDto;
import org.xiaowu.behappy.screw.entity.Database;
import org.xiaowu.behappy.screw.mapper.DatabaseMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiaowu
 */
@Service
@RequiredArgsConstructor
public class DatabaseService extends ServiceImpl<DatabaseMapper, Database> implements IService<Database> {

    public List<Database> findMenus(String name) {
        QueryWrapper<Database> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort_num");
        if (StrUtil.isNotBlank(name)) {
            queryWrapper.like("name", name);
        }
        // 查询所有数据
        List<Database> list = list(queryWrapper);
        // 找出pid为null的一级菜单
        List<Database> parentNodes = list.stream().filter(menu -> menu.getPid() == null).collect(Collectors.toList());
        // 找出一级菜单的子菜单
        for (Database menu : parentNodes) {
            // 筛选所有数据中pid=父级id的数据就是二级菜单
            menu.setChildren(list.stream().filter(m -> menu.getId().equals(m.getPid())).collect(Collectors.toList()));
        }
        return parentNodes;
    }

    /**
     * @param screwSchemaDto
     * @return
     */
    public Page<Database> allSchemas(ScrewSchemaDto screwSchemaDto) {
        // 根据角色查询当前所持有数据库
        Page<Database> screwDatabaseVos = baseMapper.pageDatabase(new Page<>(screwSchemaDto.getPageNum(), screwSchemaDto.getPageSize()),
                screwSchemaDto.getRole(),
                screwSchemaDto.getDataSourceEnum().getDatasource(),
                screwSchemaDto.getName());
        return screwDatabaseVos;
    }
}
