package org.xiaowu.behappy.screw.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.screw.constant.ResStatus;
import org.xiaowu.behappy.screw.enums.DataSourceEnum;
import org.xiaowu.behappy.screw.util.Result;
import org.xiaowu.behappy.screw.util.JDBCUtils;
import org.xiaowu.behappy.screw.dto.CheckConnDto;
import org.xiaowu.behappy.screw.entity.Database;
import org.xiaowu.behappy.screw.entity.Datasource;
import org.xiaowu.behappy.screw.mapper.DatasourceMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author xiaowu
 */
@Service
public class DatasourceServiceImpl extends ServiceImpl<DatasourceMapper, Datasource> implements IService<Datasource> {

    public List<Datasource> findAllByOp(DataSourceEnum datasource, String name) {
        LambdaQueryWrapper<Datasource> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Objects.nonNull(datasource), Datasource::getDataSource, datasource.name());
        wrapper.like(StrUtil.isNotEmpty(name), Datasource::getName, name);
        return list(wrapper);
    }

    public boolean checkConn(CheckConnDto checkConnDto) {
        String databaseType = "";
        if (checkConnDto.getDataSource().equals(DataSourceEnum.MYSQL)) {
            databaseType = "0";
        } else if (checkConnDto.getDataSource().equals(DataSourceEnum.CLICK_HOUSE)) {
            databaseType = "1";
        }
        return JDBCUtils.sqlConnTest(checkConnDto.getDataSource(), checkConnDto.getAddr(), checkConnDto.getPort(), checkConnDto.getUsername(), checkConnDto.getPassword());
    }

    public List<String> findAllDsName() {
        LambdaQueryWrapper<Datasource> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Datasource::getName);
        return list(wrapper).stream().map(Datasource::getName).collect(Collectors.toList());
    }

    public void syncDs(String name, Integer pid) {
        DatabaseServiceImpl databaseServiceImpl = SpringUtil.getBean(DatabaseServiceImpl.class);
        Datasource dbDatasource = getOne(new LambdaQueryWrapper<Datasource>()
                .eq(Datasource::getName, name));
        List<String> allDbNames = JDBCUtils.getAllDbNames(DataSourceEnum.valueOf(dbDatasource.getDataSource()), dbDatasource.getAddr(), dbDatasource.getPort(), dbDatasource.getUsername(), dbDatasource.getPassword());
        List<String> dbNames = databaseServiceImpl.list(new LambdaQueryWrapper<Database>()
                        .select(Database::getName)
                        .eq(Database::getPid, pid))
                .stream().map(Database::getName).toList();
        allDbNames.removeIf(dbNames::contains);
        List<Database> databases = new ArrayList<>();
        for (String dbName : allDbNames) {
            Database database = new Database();
            database.setPid(pid);
            database.setName(dbName);
            databases.add(database);
        }
        databaseServiceImpl.saveBatch(databases);
    }

    public Result del(Integer id) {
        DatabaseServiceImpl databaseServiceImpl = SpringUtil.getBean(DatabaseServiceImpl.class);
        Datasource datasource = getById(id);
        if (Objects.isNull(datasource)) {
            return Result.error(ResStatus.CODE_400, "数据源不存在");
        }
        Database database = databaseServiceImpl.getOne(new LambdaQueryWrapper<Database>()
                .eq(Database::getName, datasource.getName()));
        if (Objects.nonNull(database)) {
            return Result.error(ResStatus.CODE_400, "当前数据源下尚存数据库配置信息");
        }
        return Result.success(removeById(id));
    }
}
