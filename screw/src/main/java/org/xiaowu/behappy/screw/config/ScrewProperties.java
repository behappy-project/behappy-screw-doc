package org.xiaowu.behappy.screw.config;

import lombok.Data;
import org.springframework.stereotype.Component;

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
