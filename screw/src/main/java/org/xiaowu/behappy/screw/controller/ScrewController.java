package org.xiaowu.behappy.screw.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaowu.behappy.screw.util.Result;
import org.xiaowu.behappy.screw.dto.ScrewContextLoadDto;
import org.xiaowu.behappy.screw.dto.UpdateDocDto;
import org.xiaowu.behappy.screw.factory.ScrewContextLoadService;
import org.xiaowu.behappy.screw.factory.ScrewFactory;



/**
 * @author xiaowu
 */
@RestController
@RequestMapping("/api/screw")
@AllArgsConstructor
public class ScrewController {

    private final ScrewFactory screwFactory;

    /**
     * 生成数据库文档
     * @param screwContextLoadDto
     * @return
     */
    @PostMapping("/context-load")
    public Result contextLoads(@RequestBody ScrewContextLoadDto screwContextLoadDto) {
        ScrewContextLoadService screwContextLoadService = screwFactory.getScrewService(screwContextLoadDto.getDataSourceEnum().getDatasource());
        screwContextLoadService.contextLoads(screwContextLoadDto);
        return Result.success();
    }

    /**
     * 更新数据库文档
     * @param updateDocDto
     * @return
     */
    @PostMapping("/update-doc")
    public Result updateDoc(@Valid @RequestBody UpdateDocDto updateDocDto) {
        ScrewContextLoadService screwContextLoadService = screwFactory.getScrewService(updateDocDto.getDataSourceEnum().getDatasource());
        screwContextLoadService.updateDoc(updateDocDto);
        return Result.success();
    }


}
