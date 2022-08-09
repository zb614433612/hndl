package com.zbdemo.hndl.controller;

import com.zbdemo.hndl.base.ResultData;
import com.zbdemo.hndl.user.dto.LoginDto;
import com.zbdemo.hndl.user.vo.LoginVo;
import com.zbdemo.hndl.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

@RestController
@Api(value = "login", tags = "登陆相关接口")
@Validated
@Slf4j
public class LoginController {
    @Resource
    UserService userService;

    @ApiOperation(value = "用户登录")
    @PostMapping(value = "/login")
    public ResultData<LoginVo> login(@RequestBody @Validated LoginDto loginDto, HttpServletRequest request) {
        return userService.login(loginDto,request);
    }

    @ApiOperation(value = "密码登录获取随机CODE")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginAccount", value = "登陆帐号（登录名或手机号或邮箱）", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "paasCode", value = "登陆平台", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping(value = "/getLoginCode")
    @ResponseBody
    public ResultData<String> getRandomCode(@RequestParam("loginAccount") @NotNull(message = "loginAccount不能为空")  String loginAccount, @RequestParam("paasCode") @NotNull(message = "paasCode不能为空") String paasCode) {
        return userService.getRandomCode(loginAccount,paasCode);
    }

    @ApiOperation(value = "用户注销")
    @ApiImplicitParam(name = "token", value = "授权码", required = true, dataType = "String", paramType = "header")
    @PostMapping(value = "/logout")
    public ResultData logout(@RequestHeader("token") String token) {
        return userService.logout(token);
    }
}
