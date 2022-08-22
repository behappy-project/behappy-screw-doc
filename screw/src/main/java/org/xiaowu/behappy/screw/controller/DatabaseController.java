package org.xiaowu.behappy.screw.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.xiaowu.behappy.screw.common.core.enums.DataSourceEnum;
import org.xiaowu.behappy.screw.common.core.util.Result;
import org.xiaowu.behappy.screw.dto.ScrewSchemaDto;
import org.xiaowu.behappy.screw.entity.Database;
import org.xiaowu.behappy.screw.service.DatabaseHistoryService;
import org.xiaowu.behappy.screw.service.DatabaseService;

import java.util.*;

@RestController
@RequestMapping("/database")
@AllArgsConstructor
public class DatabaseController {

    private final DatabaseService databaseService;

    private final DatabaseHistoryService databaseHistoryService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Database database) {
        if (StrUtil.isBlank(database.getSortNum())) {
            database.setSortNum("999");
        }
        databaseService.saveOrUpdate(database);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        databaseService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        databaseService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping("/ids")
    public Result findAllIds() {
        return Result.success(databaseService.list().stream().map(Database::getId));
    }

    /**
     * 这里查询参数name是父级菜单
     * @param name
     * @return
     */
    @GetMapping
    public Result findAll(@RequestParam(defaultValue = "") String name) {
        return Result.success(databaseService.findDbs(name));
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(databaseService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Database> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        queryWrapper.orderByDesc("id");
        return Result.success(databaseService.page(new Page<>(pageNum, pageSize), queryWrapper));
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
     * 获取所有数据源类型
     * @return
     */
    @GetMapping("/options")
    public Result options() {
        DataSourceEnum[] values = DataSourceEnum.values();
        return Result.success(Arrays.asList(values));
    }

    /**
     * 获取所有数据更新历史,滚动更新
     * @return
     */
    @GetMapping("/history/{databaseId}/{pageSize}")
    public Result history(@PathVariable Integer databaseId,
                          @PathVariable Integer pageSize) {
        return databaseHistoryService.history(databaseId,pageSize);
    }

}

