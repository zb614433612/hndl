package com.zbdemo.hndl.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhangbing
 * @title: FindUserDto
 * @projectName hndl
 * @description: 查询用户输入类
 * @date 2022/7/18下午11:57
 */
@Data
@ApiModel(value = "查询用户输入类", description = "这是个输入对象")
public class FindUserDto {
    @ApiModelProperty(value = "用户名")
    private String userName;
    @ApiModelProperty(value = "登录名")
    private String loginName;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "年龄")
    private Integer age;
    @ApiModelProperty(value = "性别（男1,女0）")
    private Integer sex;
    @ApiModelProperty(value = "帐号状态（1启用，0禁用）")
    private Integer state;
    @ApiModelProperty(value = "所属区域")
    private Integer area;
}
