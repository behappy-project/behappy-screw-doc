package org.xiaowu.behappy.screw.factory;

import org.xiaowu.behappy.screw.dto.ScrewContextLoadDto;
import org.xiaowu.behappy.screw.dto.UpdateDocDto;

/**
 * 所有数据源执行文档的顶级接口
 * @author xiaowu
 */
public interface ScrewContextLoadService {

    /**
     * 执行文档
     */
    void screw(ScrewContextLoadDto screwContextLoadDto);

    void updateDoc(UpdateDocDto updateDocDto);

}
