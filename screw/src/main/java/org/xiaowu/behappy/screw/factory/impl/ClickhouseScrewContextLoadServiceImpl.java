package org.xiaowu.behappy.screw.factory.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.screw.common.core.constant.CommonConstant;
import org.xiaowu.behappy.screw.dto.ScrewContextLoadDto;
import org.xiaowu.behappy.screw.dto.UpdateDocDto;
import org.xiaowu.behappy.screw.factory.ScrewContextLoadService;
import org.xiaowu.behappy.screw.service.DatabaseService;

/**
 * clickhouse执行生成文档
 * @author xiaowu
 */
@Service(CommonConstant.CLICK_HOUSE)
@AllArgsConstructor
public class ClickhouseScrewContextLoadServiceImpl implements ScrewContextLoadService {

    private final DatabaseService databaseService;

    @Override
    public void contextLoads(ScrewContextLoadDto screwContextLoadDto) {
        databaseService.contextLoads(screwContextLoadDto,CommonConstant.CLICKHOUSE_URL,CommonConstant.CLICKHOUSE_DRIVER);
    }



    @Override
    public void updateDoc(UpdateDocDto updateDocDto) {
        databaseService.updateDocs(updateDocDto,CommonConstant.CLICKHOUSE_URL,CommonConstant.CLICKHOUSE_DRIVER);
    }
}
