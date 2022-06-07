package org.xiaowu.behappy.screw.factory;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 工厂
 * @author xiaowu
 */
@Component
@AllArgsConstructor
public class ScrewFactory {

    private final Map<String, ScrewContextLoadService> screwServiceMap;

    /**
     * 这里通过SCREW_*获取bean
     * @param dataSource
     * @return
     */
    public ScrewContextLoadService getScrewService(String dataSource){
        return screwServiceMap.get(dataSource);
    }

}
