package org.xiaowu.behappy.screw.util;

import cn.hutool.extra.spring.SpringUtil;
import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.experimental.UtilityClass;
import org.xiaowu.behappy.screw.common.core.config.ScrewProperties;
import org.xiaowu.behappy.screw.entity.Database;

import javax.sql.DataSource;
import java.io.File;

/**
 * 生成
 * @author xiaowu
 */
@UtilityClass
public class ScrewUtils {

    public void loadDoc(ScrewProperties.ScrewDataSourceProperty dataSourceProperty, String url, Database screwSchema, String datasource) {

        ScrewProperties screwProperties = SpringUtil.getBean(ScrewProperties.class);

        // 工作目录
        String proFilePath = System.getProperty("user.dir") + File.separator + "doc" + File.separator + datasource;
        //生成配置
        EngineConfig engineConfig = EngineConfig.builder()
                //生成文件路径
                .fileOutputDir(proFilePath)
                //打开目录
                .openOutputDir(false)
                //文件类型
                .fileType(EngineFileType.HTML)
                //生成模板实现
                .produceType(EngineTemplateType.freemarker)
                //自定义文件名称
                .fileName(screwSchema.getName()).build();

        ProcessConfig processConfig = getProcessConfig(screwProperties);
        Configuration config = Configuration.builder()
                //版本
                .version(screwProperties.getVersion())
                //描述
                .description(screwSchema.getDescription())
                //数据源
                .dataSource(getDataSource(dataSourceProperty.getDriverClassName(), url, dataSourceProperty.getUsername(), dataSourceProperty.getPassword()))
                //生成配置
                .engineConfig(engineConfig)
                //生成配置
                .produceConfig(processConfig)
                .title(screwSchema.getName())
                .build();
        //执行生成
        new DocumentationExecute(config).execute();
    }

    private ProcessConfig getProcessConfig(ScrewProperties screwProperties) {
        ProcessConfig processConfig = ProcessConfig.builder()
                //指定生成逻辑、当存在指定表、指定表前缀、指定表后缀时，将生成指定表，其余表不生成、并跳过忽略表配置
                //忽略表名
                .ignoreTableName(screwProperties.getIgnoreTableName())
                //忽略表前缀
                .ignoreTablePrefix(screwProperties.getIgnorePrefix())
                //忽略表后缀
                .ignoreTableSuffix(screwProperties.getIgnoreSuffix()).build();
        return processConfig;
    }

    private DataSource getDataSource(String driver, String url, String username, String password) {
        //数据源
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(driver);
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        //对于mysql,设置可以获取tables remarks信息
        hikariConfig.addDataSourceProperty("useInformationSchema", "true");
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setMaximumPoolSize(5);
        DataSource dataSource = new HikariDataSource(hikariConfig);
        return dataSource;
    }
}
