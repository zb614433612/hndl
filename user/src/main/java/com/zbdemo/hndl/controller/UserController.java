package com.zbdemo.hndl.controller;

import com.github.pagehelper.Page;
import com.zbdemo.hndl.base.PageQuery;
import com.zbdemo.hndl.base.ResultData;
import com.zbdemo.hndl.service.UserService;
import com.zbdemo.hndl.user.dto.FindUserDto;
import com.zbdemo.hndl.user.dto.ForgetPasswordDto;
import com.zbdemo.hndl.user.dto.InsertUserDto;
import com.zbdemo.hndl.user.dto.UpdateUserDto;
import com.zbdemo.hndl.user.vo.FindUserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author zhangbing
 * @title: UserController
 * @projectName hndl
 * @description: 用户操作相关接口
 * @date 2022/7/16上午1:00
 */
@RestController
@Api(value = "user", tags = "用户操作相关接口")
@RequestMapping("/user")
@Validated
public class UserController {
    @Resource
    UserService userService;

    @ApiOperation(value = "新增用户")
    @PostMapping(value = "/insertUser")
    public ResultData insertUser(@RequestBody @Validated InsertUserDto insertUserDto) {
        return userService.insertUser(insertUserDto);
    }

    @ApiOperation(value = "修改用户信息")
    @PostMapping(value = "/updateUser")
    public ResultData updateUser(@RequestBody @Validated UpdateUserDto updateUserDto) {
        return userService.updateUser(updateUserDto);
    }

    @ApiOperation(value = "删除用户信息")
    @PostMapping(value = "/deleteUser")
    public ResultData deleteUser(@RequestBody @NotNull(message = "userIds不能为空") List<String> userIds) {
        return userService.deleteUser(userIds);
    }

    @ApiOperation(value = "分页用户列表")
    @PostMapping(value = "/pageUserList")
    public ResultData<Page<FindUserVo>> pageUserList(@RequestBody PageQuery<FindUserDto> pageQuery,@RequestHeader("token") String token) {
        return userService.pageUserList(pageQuery);
    }

    @ApiOperation(value = "停用用户")
    @GetMapping(value = "/stopUser")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "query")
    })
    public ResultData stopUser(@RequestParam @NotNull(message = "userId不能为空") String userId) {
        return userService.stopUser(userId);
    }

    @ApiOperation(value = "修改密码")
    @PostMapping(value = "/updatePassword")
    public ResultData updatePassword(@RequestBody ForgetPasswordDto forgetPasswordDto) {
        return userService.updatePassword(forgetPasswordDto);
    }

    @ApiOperation(value = "重置密码（管理员权限）")
    @GetMapping(value = "/resetPassword")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID（需要重置的用户）", required = true, dataType = "String", paramType = "query")
    })
    public ResultData resetPassword(@RequestParam @NotNull(message = "userIds不能为空") String userId) {
        return userService.resetPassword(userId);
    }
}
