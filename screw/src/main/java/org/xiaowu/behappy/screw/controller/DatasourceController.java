package org.xiaowu.behappy.screw.controller;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.xiaowu.behappy.screw.common.core.enums.DataSourceEnum;
import org.xiaowu.behappy.screw.common.core.util.Result;
import org.xiaowu.behappy.screw.dto.CheckConnDto;
import org.xiaowu.behappy.screw.entity.Datasource;
import org.xiaowu.behappy.screw.service.DatasourceService;

import javax.validation.Valid;
import java.util.List;

/**
 * @author 94391
 */
@RestController
@RequestMapping("/datasource")
@AllArgsConstructor
public class DatasourceController {

    private final DatasourceService datasourceService;

    /**
     * 新增或者更新
     * @param datasource
     * @return
     */
    @PostMapping
    public Result saveOrUpdate(@RequestBody Datasource datasource) {
        return Result.success(datasourceService.saveOrUpdate(datasource));
    }


    /**
     * 删除
     * @return
     */
    @DeleteMapping("/{id}")
    public Result del(@PathVariable Integer id) {
        return Result.success(datasourceService.removeById(id));
    }


    /**
     * 查询所有(按条件)
     * @return
     */
    @GetMapping
    public Result findAllByOp(@RequestParam(value = "dataSource",required = false) DataSourceEnum dataSource,
                              @RequestParam(value = "name",required = false) String name) {
        List<Datasource> datasources = datasourceService.findAllByOp(dataSource, name);
        return Result.success(datasources);
    }


    /**
     * 指定id查询
     * @return
     */
    @GetMapping("/{id}")
    public Result findById(@PathVariable Integer id) {
        return Result.success(datasourceService.getById(id));
    }


    /**
     * 测试连接
     * @return
     */
    @PostMapping("/check-conn")
    public Result checkConn(@Valid @RequestBody CheckConnDto checkConnDto) {
        boolean success = datasourceService.checkConn(checkConnDto);
        return Result.success(success);
    }


    /**
     * 查询所有数据源名称
     * @return
     */
    @GetMapping("/ds-name")
    public Result findAllDsName() {
        List<String> dsNames = datasourceService.findAllDsName();
        return Result.success(dsNames);
    }


    /**
     * 同步数据源
     * @return
     */
    @GetMapping("/sync")
    public Result syncDs(@RequestParam String name,
                         @RequestParam Integer pid) {
        datasourceService.syncDs(name, pid);
        return Result.success();
    }

}

