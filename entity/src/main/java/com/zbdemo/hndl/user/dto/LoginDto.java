package com.zbdemo.hndl.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zhangbing
 * @title: LoginDto
 * @projectName hndl
 * @description: 用户登陆输入类
 * @date 2022/7/15下午5:33
 */
@Data
@ApiModel(value = "登录注册-输入对象", description = "这是个输入对象")
public class LoginDto {
    @ApiModelProperty(value = "登陆类型（1登录名or手机号or邮箱+密码;2手机号+验证码）", example = "1", required = true)
    @NotNull
    private int longinType;

    @ApiModelProperty(value = "平台码(PC,WXAPP)", example = "paasCode", required = true)
    @NotNull
    private String paasCode;

    @ApiModelProperty(value = "登陆帐号（登录名或手机号或邮箱）", example = "loginAccount", required = true)
    @NotNull
    private String loginAccount;

    @ApiModelProperty(value = "登陆码（密码或验证码）", example = "loginCode", required = true)
    @NotNull
    private String loginCode;
}
