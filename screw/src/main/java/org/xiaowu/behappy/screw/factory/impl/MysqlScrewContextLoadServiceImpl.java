package org.xiaowu.behappy.screw.factory.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.screw.constant.CommonConstant;
import org.xiaowu.behappy.screw.dto.ScrewContextLoadDto;
import org.xiaowu.behappy.screw.dto.UpdateDocDto;
import org.xiaowu.behappy.screw.factory.ScrewContextLoadService;
import org.xiaowu.behappy.screw.service.DatabaseServiceImpl;

/**
 * mysql执行生成文档
 * 注意bean的名字
 * @author xiaowu
 */
@Service(CommonConstant.MYSQL)
@AllArgsConstructor
public class MysqlScrewContextLoadServiceImpl implements ScrewContextLoadService {

    private final DatabaseServiceImpl databaseServiceImpl;

    @Override
    public void contextLoads(ScrewContextLoadDto screwContextLoadDto) {
        databaseServiceImpl.contextLoads(screwContextLoadDto,CommonConstant.MYSQL_URL,CommonConstant.MYSQL_DRIVER);
    }

    @Override
    public void updateDoc(UpdateDocDto updateDocDto) {
        databaseServiceImpl.updateDocs(updateDocDto, CommonConstant.MYSQL_URL,CommonConstant.MYSQL_DRIVER);
    }



}
