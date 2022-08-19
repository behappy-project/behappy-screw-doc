package org.xiaowu.behappy.screw.common.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * screw配置
 * @author xiaowu
 */
@Data
@Component
public class ScrewProperties {

    public String getVersion() {
        return this.getClass().getPackage().getImplementationVersion();
    }
}
