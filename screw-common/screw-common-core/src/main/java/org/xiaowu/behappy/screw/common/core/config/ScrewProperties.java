package org.xiaowu.behappy.screw.common.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * screw配置
 * @author xiaowu
 */
@Data
@Configuration
@ConfigurationProperties(value = "screw")
public class ScrewProperties {

    /**
     * 忽略表
     */
    private List<String> ignoreTableName = new ArrayList<>();

    /**
     * 忽略表前缀
     */
    private List<String> ignorePrefix = new ArrayList<>();

    /**
     * 忽略表后缀
     */
    private List<String> ignoreSuffix = new ArrayList<>();

    private Map<String, ScrewDataSourceProperty> datasource;

    @Data
    public static class ScrewDataSourceProperty {

        private String ip;

        private Integer port;

        private String username;

        private String password;

        private String driverClassName;

    }

    public String getVersion() {
        return this.getClass().getPackage().getImplementationVersion();
    }
}
