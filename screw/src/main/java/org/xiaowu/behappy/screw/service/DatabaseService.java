package org.xiaowu.behappy.screw.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.xiaowu.behappy.screw.common.core.util.AuthContextHolder;
import org.xiaowu.behappy.screw.dto.ScrewContextLoadDto;
import org.xiaowu.behappy.screw.dto.ScrewSchemaDto;
import org.xiaowu.behappy.screw.dto.UpdateDocDto;
import org.xiaowu.behappy.screw.entity.Database;
import org.xiaowu.behappy.screw.entity.DatabaseHistory;
import org.xiaowu.behappy.screw.entity.Datasource;
import org.xiaowu.behappy.screw.mapper.DatabaseHistoryMapper;
import org.xiaowu.behappy.screw.mapper.DatabaseMapper;
import org.xiaowu.behappy.screw.util.ScrewUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiaowu
 */
@Service
@AllArgsConstructor
public class DatabaseService extends ServiceImpl<DatabaseMapper, Database> implements IService<Database> {

    private final DatabaseHistoryMapper databaseHistoryMapper;

    private final HttpServletRequest httpServletRequest;

    private final DatasourceService datasourceService;

    private final RoleDatabaseService roleDatabaseService;

    public List<Database> findDbs(String name) {
        // 查询所有数据
        List<Database> list = baseMapper.findDbsOrderBySortNum(name);
        // 找出pid为null的一级菜单
        List<Database> parentNodes = list.stream().filter(menu -> menu.getPid() == null).collect(Collectors.toList());
        // 找出一级菜单的子菜单
        for (Database menu : parentNodes) {
            // 筛选所有数据中pid=父级id的数据就是二级菜单
            menu.setChildren(list.stream().filter(m -> menu.getId().equals(m.getPid()))
                    .sorted(Comparator.comparingInt(o -> Integer.parseInt(o.getSortNum())))
                    .sorted(Comparator.comparingInt(Database::getId))
                    .collect(Collectors.toList()));
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
                screwSchemaDto.getDataSource().getDatasource(),
                screwSchemaDto.getName(),
                screwSchemaDto.getDatabase());
        List<Database> records = screwDatabaseVos.getRecords();
        for (Database record : records) {
            record.setDsName(screwSchemaDto.getName());
        }
        return screwDatabaseVos;
    }

    public void contextLoads(ScrewContextLoadDto screwContextLoadDto, String jdbcUrl, String driverClass) {
        if (!CollectionUtils.isEmpty(screwContextLoadDto.getIds())) {
            // 先查出这些数据库
            List<Database> databases = listByIds(screwContextLoadDto.getIds());
            for (Database database : databases) {
                Database parentDatabase = getOne(new LambdaQueryWrapper<Database>()
                        .eq(Database::getId, database.getPid()));
                // 再查出数据源配置信息
                Datasource datasource = datasourceService.getOne(new LambdaQueryWrapper<Datasource>()
                        .eq(Datasource::getName, parentDatabase.getName()));
                ScrewUtils.loadDoc(driverClass, jdbcUrl, datasource, database);
            }
        }
    }

    public void updateDocs(UpdateDocDto updateDocDto, String jdbcUrl, String driverClass) {
        // 先查出数据库
        Database database = getById(updateDocDto.getId());
        Database parentDatabase = getOne(new LambdaQueryWrapper<Database>()
                .eq(Database::getId, database.getPid()));
        // 再查出数据源配置信息
        Datasource datasource = datasourceService.getOne(new LambdaQueryWrapper<Datasource>()
                .eq(Datasource::getName, parentDatabase.getName()));
        ScrewUtils.loadDoc(driverClass, jdbcUrl, datasource, database);
        DatabaseHistory databaseHistory = new DatabaseHistory();
        databaseHistory.setDatabaseId(database.getId());
        databaseHistory.setDatabaseName(database.getName());
        databaseHistory.setUpdateTime(new Date());
        databaseHistory.setUpdateUser(AuthContextHolder.getUserName(httpServletRequest));
        databaseHistory.setDescription(updateDocDto.getUpdateDocContent());
        databaseHistoryMapper.insert(databaseHistory);
    }

    public void delete(Integer id) {
        Database dbDatabase = getById(id);
        List<Integer> ids = new ArrayList<>();
        // 父级菜单
        if (dbDatabase.getPid() == null) {
            LambdaQueryWrapper<Database> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(Database::getId);
            wrapper.eq(Database::getPid, id);
            List<Database> list = list(wrapper);
            if (!CollectionUtils.isEmpty(list)) {
                ids = list.stream().map(Database::getId).collect(Collectors.toList());
            }
        }
        ids.add(id);
        if (removeBatchByIds(ids)) {
            // 删除role-database关联表数据
            roleDatabaseService.removeByDatabaseId(ids);
        }
    }
}
