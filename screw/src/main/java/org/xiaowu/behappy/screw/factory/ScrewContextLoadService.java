package org.xiaowu.behappy.screw.factory;

import org.xiaowu.behappy.screw.dto.ScrewContextLoadDto;

/**
 * 所有数据源执行文档的顶级接口
 * @author xiaowu
 */
public interface ScrewContextLoadService {

    /**
     * 执行文档
     */
    void screw(ScrewContextLoadDto screwContextLoadDto);

}
