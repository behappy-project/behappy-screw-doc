package org.xiaowu.behappy.screw.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.xiaowu.behappy.screw.util.Result;
import org.xiaowu.behappy.screw.entity.Role;
import org.xiaowu.behappy.screw.service.RoleServiceImpl;

import java.util.Collections;
import java.util.List;


/**
 * @author xiaowu
 */
@RestController
@RequestMapping("/api/role")
@AllArgsConstructor
public class RoleController {

    private final RoleServiceImpl roleServiceImpl;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Role role) {
        roleServiceImpl.saveRole(role);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        roleServiceImpl.deleteBatch(Collections.singletonList(id));
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        roleServiceImpl.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(roleServiceImpl.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(roleServiceImpl.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        queryWrapper.orderByDesc("id");
        return Result.success(roleServiceImpl.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
     * 绑定角色和数据库的关系
     * @param roleId 角色id
     * @param databases 菜单id数组
     * @return
     */
    @PostMapping("/roleDatabase/{roleId}")
    public Result roleDatabase(@PathVariable Integer roleId, @RequestBody List<Integer> databases) {
        roleServiceImpl.setRoleDatabase(roleId, databases);
        return Result.success();
    }

    /**
     * 这里需要排除父id, el-tree组件如果带了父级id,则会勾选全部
     * @param roleId
     * @return
     */
    @GetMapping("/roleDatabase/{roleId}")
    public Result getRoleDatabase(@PathVariable Integer roleId) {
        return Result.success( roleServiceImpl.getRoleDatabase(roleId));
    }

}

