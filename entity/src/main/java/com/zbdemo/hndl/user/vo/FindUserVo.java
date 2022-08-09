package com.zbdemo.hndl.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhangbing
 * @title: UserVo
 * @projectName hndl
 * @description: 用户信息输出类
 * @date 2022/7/18下午11:47
 */
@Data
@ApiModel(value = "用户信息输出类", description = "这是个输入对象")
public class FindUserVo {
    @ApiModelProperty(value = "主键ID")
    private String id;
    @ApiModelProperty(value = "权限ID")
    private String roleId;
    @ApiModelProperty(value = "权限名称")
    private String roleName;
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
    @ApiModelProperty(value = "证件号")
    private String idCard;
    @ApiModelProperty(value = "证件类型")
    private Integer idCardType;
    @ApiModelProperty(value = "帐号状态（1启用，0禁用）")
    private Integer state;
    @ApiModelProperty(value = "平台码，多平台逗号分割(PC,WXAPP)")
    private String paasCode;
    @ApiModelProperty(value = "是否修改密码（0未修改，1已修改）")
    private Integer updatePassword;
    @ApiModelProperty(value = "所属区域")
    private Integer area;
    @ApiModelProperty(value = "用户是否在线(0不在线，1在线)")
    private Integer online;
    @ApiModelProperty(value = "创建时间")
    private Long createTime;
    @ApiModelProperty(value = "更新时间")
    private Long updateTime;
}
