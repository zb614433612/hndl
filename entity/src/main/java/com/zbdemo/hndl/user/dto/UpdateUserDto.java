package com.zbdemo.hndl.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhangbing
 * @title: UpdateUserDto
 * @projectName hndl
 * @description: 修改用户信息输入类
 * @date 2022/7/18下午10:28
 */
@Data
@ApiModel(value = "修改用户信息输入类", description = "这是个输入对象")
public class UpdateUserDto {
    @ApiModelProperty(value = "用户ID", example = "1", required = true)
    private String id;
    @ApiModelProperty(value = "权限ID")
    private String roleId;
    @ApiModelProperty(value = "用户名", example = "zhangsan")
    private String userName;
    @ApiModelProperty(value = "登录名", example = "zhangsan123")
    private String loginName;
    @ApiModelProperty(value = "手机号", example = "12345678912")
    private String phone;
    @ApiModelProperty(value = "邮箱", example = "1@hndl.com")
    private String email;
    @ApiModelProperty(value = "年龄", example = "1")
    private Integer age;
    @ApiModelProperty(value = "性别（男1,女0）", example = "1")
    private Integer sex;
    @ApiModelProperty(value = "证件号", example = "1")
    private String idCard;
    @ApiModelProperty(value = "证件类型", example = "1")
    private Integer idCardType;
    @ApiModelProperty(value = "所属区域")
    private Integer area;
}
