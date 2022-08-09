package com.zbdemo.hndl.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author zhangbing
 * @title: UserDto
 * @projectName hndl
 * @description: 新增用户输入类
 * @date 2022/7/16上午1:18
 */
@Data
@ApiModel(value = "新增用户输入类", description = "这是个输入对象")
public class InsertUserDto {
    @ApiModelProperty(value = "权限ID")
    private String roleId;
    @ApiModelProperty(value = "用户名", example = "zhangsan", required = true)
    @NotNull(message = "用户名不能为空")
    private String userName;
    @ApiModelProperty(value = "登录名", example = "zhangsan123", required = true)
    @NotNull(message = "登陆名不能为空")
    private String loginName;
    @ApiModelProperty(value = "手机号", example = "12345678912")
    @Length(min = 11, max = 11, message = "手机号只能为11位")
    @Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误")
    private String phone;
    @ApiModelProperty(value = "邮箱", example = "1@hndl.com")
    @Email
    private String email;
    @ApiModelProperty(value = "年龄", example = "1")
    private Integer age;
    @ApiModelProperty(value = "性别（男1,女0）", example = "1")
    private Integer sex;
    @ApiModelProperty(value = "证件号", example = "1")
    private String idCard;
    @ApiModelProperty(value = "证件类型", example = "1")
    private Integer idCardType;
    @ApiModelProperty(value = "平台码，多平台逗号分割(PC,WXAPP)", example = "PC", required = true)
    @NotNull(message = "平台码不能为空")
    private String paasCode;
    @ApiModelProperty(value = "帐号状态（1启用，0禁用）", example = "1")
    private Integer state;
    @ApiModelProperty(value = "所属区域")
    private Integer area;
}
