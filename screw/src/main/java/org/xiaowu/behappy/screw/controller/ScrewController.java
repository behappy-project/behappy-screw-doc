package org.xiaowu.behappy.screw.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.xiaowu.behappy.screw.common.core.enums.DataSourceEnum;
import org.xiaowu.behappy.screw.common.core.util.Result;
import org.xiaowu.behappy.screw.dto.ScrewContextLoadDto;
import org.xiaowu.behappy.screw.dto.ScrewSchemaDto;
import org.xiaowu.behappy.screw.dto.UpdateDocDto;
import org.xiaowu.behappy.screw.entity.Database;
import org.xiaowu.behappy.screw.factory.ScrewContextLoadService;
import org.xiaowu.behappy.screw.factory.ScrewFactory;
import org.xiaowu.behappy.screw.service.DatabaseHistoryService;
import org.xiaowu.behappy.screw.service.DatabaseService;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * @author xiaowu
 */
@RestController
@RequestMapping("/screw")
@AllArgsConstructor
public class ScrewController {

    private final ScrewFactory screwFactory;

    private final DatabaseService databaseService;

    private final DatabaseHistoryService databaseHistoryService;

    /**
     * 生成数据库文档
     * @param screwContextLoadDto
     * @return
     */
    @PostMapping("/context-load")
    public Result contextLoads(@RequestBody ScrewContextLoadDto screwContextLoadDto) {
        ScrewContextLoadService screwContextLoadService = screwFactory.getScrewService(screwContextLoadDto.getDataSourceEnum().getDatasource());
        screwContextLoadService.screw(screwContextLoadDto);
        return Result.success();
    }

    /**
     * 更新数据库文档
     * @param updateDocDto
     * @return
     */
    @PostMapping("/update-doc")
    public Result updateDoc(@RequestBody UpdateDocDto updateDocDto) {
        ScrewContextLoadService screwContextLoadService = screwFactory.getScrewService(updateDocDto.getDataSourceEnum().getDatasource());
        screwContextLoadService.updateDoc(updateDocDto);
        return Result.success();
    }

    /**
     * 根据数据源和角色查询数据库
     * @param screwSchemaDto
     * @return
     */
    @PostMapping("/schemas")
    public Result allSchemas(@RequestBody ScrewSchemaDto screwSchemaDto) {
        Page<Database> list = databaseService.allSchemas(screwSchemaDto);
        return Result.success(list);
    }

    /**
     * 获取所有数据源
     * @return
     */
    @GetMapping("/options")
    public Result options() {
        List<Map<String, DataSourceEnum>> linkedList = new LinkedList<>();
        for (DataSourceEnum value : DataSourceEnum.values()) {
            Map<String,DataSourceEnum> dataSourceEnumMap = new LinkedHashMap<>();
            dataSourceEnumMap.put("value",value);
            linkedList.add(dataSourceEnumMap);
        }
        return Result.success(linkedList);
    }

    /**
     * 获取所有数据更新历史
     * @return
     */
    @GetMapping("/history/{databaseId}")
    public Result history(@PathVariable int databaseId) {
        return databaseHistoryService.history(databaseId);
    }

}
