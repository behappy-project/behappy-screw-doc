package org.xiaowu.behappy.screw.controller;


import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.xiaowu.behappy.screw.common.core.constant.ResStatus;
import org.xiaowu.behappy.screw.common.core.util.Result;
import org.xiaowu.behappy.screw.dto.UserDto;
import org.xiaowu.behappy.screw.dto.UserPasswordDto;
import org.xiaowu.behappy.screw.entity.User;
import org.xiaowu.behappy.screw.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.xiaowu.behappy.screw.common.core.constant.CommonConstant.DEFAULT_PASS;
import static org.xiaowu.behappy.screw.common.core.constant.CommonConstant.REGISTER_ENABLE;


@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/register-enable")
    public Result registerEnable() {
        String registerEnable = System.getenv(REGISTER_ENABLE);
        if (StrUtil.isEmpty(registerEnable)){
            return Result.success(Boolean.TRUE);
        }
        return Result.success(Boolean.valueOf(registerEnable));
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserDto userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(ResStatus.CODE_400, "参数错误");
        }
        return Result.success(userService.register(userDTO));
    }

    @PostMapping("/login")
    public Result login(@RequestBody UserDto userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(ResStatus.CODE_400, "参数错误");
        }
        UserDto dto = userService.login(userDTO);
        return Result.success(dto);
    }

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody User user) {
        // 新增用户操作
        if (Objects.isNull(user.getId())){
            // 用户密码 md5加密
            user.setPassword(SecureUtil.md5(DEFAULT_PASS));
        }
        userService.saveOrUpdate(user);
        return Result.success(userService.saveUser(user));
    }

    /**
     * 修改密码
     * @param userPasswordDTO
     * @return
     */
    @PostMapping("/password")
    public Result password(@RequestBody UserPasswordDto userPasswordDTO) {
        userService.updatePassword(userPasswordDTO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        userService.deleteBatch(Collections.singletonList(id));
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        userService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(userService.list());
    }

    @GetMapping("/role/{role}")
    public Result findUsersByRole(@PathVariable String role) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role", role);
        List<User> list = userService.list(queryWrapper);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(userService.findByUserId(id));
    }

    @GetMapping("/username/{username}")
    public Result findByUsername(@PathVariable String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return Result.success(userService.getOne(queryWrapper));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                             @RequestParam Integer pageSize,
                             @RequestParam(defaultValue = "") String username,
                             @RequestParam(defaultValue = "") String email,
                             @RequestParam(defaultValue = "") String address) {
        return Result.success(userService.findPage(new Page<>(pageNum, pageSize), username, email, address));
    }

}

