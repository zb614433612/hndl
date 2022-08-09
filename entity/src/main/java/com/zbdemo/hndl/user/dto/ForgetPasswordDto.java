package com.zbdemo.hndl.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author zhangbing
 * @title: ForgetPasswordDto
 * @projectName hndl
 * @description: 修改密码
 * @date 2022/7/28上午1:15
 */
@Data
@ApiModel(value = "修改密码输入类", description = "这是个输入对象")
public class ForgetPasswordDto {
    @ApiModelProperty(value = "用户id", example = "", required = true)
    @NotBlank(message = "用户id不能为空")
    private String id;

    @ApiModelProperty(value = "旧密码(MD5加密传输)", example = "", required = true)
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    @ApiModelProperty(value = "新密码(MD5加密传输)", example = "", required = true)
    @NotBlank(message = "新密码不能为空")
    private String firstPassword;

    @ApiModelProperty(value = "确认密码(MD5加密传输)", example = "", required = true)
    @NotBlank(message = "验证密码不能为空")
    private String secondPassword;
}
