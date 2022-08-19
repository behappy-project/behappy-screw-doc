package org.xiaowu.behappy.screw.factory.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.screw.common.core.constant.CommonConstant;
import org.xiaowu.behappy.screw.dto.ScrewContextLoadDto;
import org.xiaowu.behappy.screw.dto.UpdateDocDto;
import org.xiaowu.behappy.screw.factory.ScrewContextLoadService;
import org.xiaowu.behappy.screw.service.DatabaseService;

/**
 * mysql执行生成文档
 * 注意bean的名字
 * @author xiaowu
 */
@Service(CommonConstant.MYSQL)
@AllArgsConstructor
public class MysqlScrewContextLoadServiceImpl implements ScrewContextLoadService {

    private final DatabaseService databaseService;

    @Override
    public void contextLoads(ScrewContextLoadDto screwContextLoadDto) {
        databaseService.contextLoads(screwContextLoadDto,CommonConstant.MYSQL_URL,CommonConstant.MYSQL_DRIVER);
    }

    @Override
    public void updateDoc(UpdateDocDto updateDocDto) {
        databaseService.updateDocs(updateDocDto, CommonConstant.MYSQL_URL,CommonConstant.MYSQL_DRIVER);
    }



}
