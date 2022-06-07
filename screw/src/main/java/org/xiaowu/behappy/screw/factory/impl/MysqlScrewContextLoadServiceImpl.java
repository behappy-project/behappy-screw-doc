package org.xiaowu.behappy.screw.factory.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.xiaowu.behappy.screw.common.core.config.ScrewProperties;
import org.xiaowu.behappy.screw.common.core.constant.CommonConstant;
import org.xiaowu.behappy.screw.dto.ScrewContextLoadDto;
import org.xiaowu.behappy.screw.entity.Database;
import org.xiaowu.behappy.screw.factory.ScrewContextLoadService;
import org.xiaowu.behappy.screw.service.DatabaseService;
import org.xiaowu.behappy.screw.util.ScrewUtils;

import java.util.List;

/**
 * mysql执行生成文档
 * 注意bean的名字
 * @author xiaowu
 */
@Service(CommonConstant.MYSQL)
@AllArgsConstructor
public class MysqlScrewContextLoadServiceImpl implements ScrewContextLoadService {

    private final ScrewProperties screwProperties;

    private final DatabaseService databaseService;

    @Override
    public void screw(ScrewContextLoadDto screwContextLoadDto) {
        String datasource = screwContextLoadDto.getDataSourceEnum().getDatasource();
        ScrewProperties.ScrewDataSourceProperty dataSourceProperty = screwProperties.getDatasource().get(datasource);
        if (!CollectionUtils.isEmpty(screwContextLoadDto.getIds())) {
            // 先查出这些数据库
            List<Database> databases = databaseService.listByIds(screwContextLoadDto.getIds());
            for (Database database : databases) {
                // host, port, database
                String url = String.format(CommonConstant.MYSQL_URL,dataSourceProperty.getIp(),dataSourceProperty.getPort(),database.getName());
                ScrewUtils.loadDoc(dataSourceProperty, url, database,datasource);
            }
        }
    }


}
