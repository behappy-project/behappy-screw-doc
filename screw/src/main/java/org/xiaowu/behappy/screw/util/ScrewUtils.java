package org.xiaowu.behappy.screw.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.xiaowu.behappy.screw.core.Configuration;
import org.xiaowu.behappy.screw.core.engine.EngineConfig;
import org.xiaowu.behappy.screw.core.engine.EngineFileType;
import org.xiaowu.behappy.screw.core.engine.EngineTemplateType;
import org.xiaowu.behappy.screw.core.execute.DocumentationExecute;
import org.xiaowu.behappy.screw.core.process.ProcessConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.experimental.UtilityClass;
import org.xiaowu.behappy.screw.config.ScrewProperties;
import org.xiaowu.behappy.screw.entity.Database;
import org.xiaowu.behappy.screw.entity.Datasource;

import javax.sql.DataSource;
import java.io.File;

/**
 * load doc by screw
 * @author xiaowu
 */
@UtilityClass
public class ScrewUtils {

    public void loadDoc(String driverClass,
                        String jdbcUrl,
                        Datasource datasource,
                        Database database) {
        // host, port, database
        String url = String.format(jdbcUrl, datasource.getAddr(), datasource.getPort(), database.getName());
        ScrewProperties screwProperties = SpringUtil.getBean(ScrewProperties.class);

        // 工作目录
        String proFilePath = System.getProperty("user.dir") + File.separator + "doc" + File.separator + datasource.getName();
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
                .fileName(database.getName()).build();

        ProcessConfig processConfig = getProcessConfig(datasource);
        Configuration config = Configuration.builder()
                //版本
                .version(screwProperties.getVersion())
                //描述
                .description(database.getDescription())
                //数据源
                .dataSource(getDataSource(driverClass, url, datasource.getUsername(), datasource.getPassword()))
                //生成配置
                .engineConfig(engineConfig)
                //生成配置
                .produceConfig(processConfig)
                .title(database.getName())
                .build();
        //执行生成
        new DocumentationExecute(config).execute();
    }

    private ProcessConfig getProcessConfig(Datasource datasource) {
        return ProcessConfig.builder()
                //指定生成逻辑、当存在指定表、指定表前缀、指定表后缀时，将生成指定表，其余表不生成、并跳过忽略表配置
                //忽略表名
                .ignoreTableName(StrUtil.split(datasource.getIgnoreTableName(),","))
                //忽略表前缀
                .ignoreTablePrefix(StrUtil.split(datasource.getIgnorePrefix(),","))
                //忽略表后缀
                .ignoreTableSuffix(StrUtil.split(datasource.getIgnoreSuffix(),",")).build();
    }

    private DataSource getDataSource(String driver, String url, String username, String password) {
        //数据源
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(driver);
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        //对于mysql,设置此属性可以获取tables remarks信息
        hikariConfig.addDataSourceProperty("useInformationSchema", "true");
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setMaximumPoolSize(5);
        return new HikariDataSource(hikariConfig);
    }
}
