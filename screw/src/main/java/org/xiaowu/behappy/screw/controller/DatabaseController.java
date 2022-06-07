package org.xiaowu.behappy.screw.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.xiaowu.behappy.screw.common.core.constant.CommonConstant;
import org.xiaowu.behappy.screw.common.core.util.Result;
import org.xiaowu.behappy.screw.entity.Database;
import org.xiaowu.behappy.screw.entity.Dict;
import org.xiaowu.behappy.screw.mapper.DictMapper;
import org.xiaowu.behappy.screw.service.DatabaseService;

import java.util.List;

@RestController
@RequestMapping("/database")
@AllArgsConstructor
public class DatabaseController {

    private final DatabaseService databaseService;

    private final DictMapper dictMapper;

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

    @GetMapping
    public Result findAll(@RequestParam(defaultValue = "") String name) {
        return Result.success(databaseService.findMenus(name));
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

    @GetMapping("/icons")
    public Result getIcons() {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", CommonConstant.DICT_TYPE_ICON);
        return Result.success(dictMapper.selectList(queryWrapper));
    }

}

