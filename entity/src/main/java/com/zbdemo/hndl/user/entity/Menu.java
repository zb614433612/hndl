package com.zbdemo.hndl.user.entity;

import com.zbdemo.hndl.groups.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zhangbing
 * @title: Menu
 * @projectName hndl
 * @description: 权限菜单
 * @date 2022/7/15下午10:21
 */
@Data
@ApiModel(value = "权限菜单实体对象", description = "这是个实体对象")
public class Menu {
    @ApiModelProperty(value = "主键ID")
    @NotNull(groups = {UpdateGroup.class},message = "id不能为空")
    private String id;
    @ApiModelProperty(value = "父菜单ID", example = "父菜单ID")
    private String pid;
    @ApiModelProperty(value = "菜单类型", example = "菜单类型")
    private Integer type;
    @ApiModelProperty(value = "菜单标题", example = "菜单标题", required = true)
    @NotNull(groups = {UpdateGroup.class}, message = "菜单标题不能为空")
    private String title;
    @ApiModelProperty(value = "菜单排序", example = "菜单标题")
    private Integer menuSort;
    @ApiModelProperty(value = "菜单图标", example = "菜单图标")
    private String icon;
    @ApiModelProperty(value = "菜单跳转路径", example = "菜单跳转路径")
    private String path;
    @ApiModelProperty(value = "是否外部跳转", example = "是否外部跳转")
    private Integer frame;
    @ApiModelProperty(value = "菜单备注", example = "菜单备注")
    private String remark;
    @ApiModelProperty(value = "创建时间")
    private Long createTime;
    @ApiModelProperty(value = "更新时间")
    private Long updateTime;
    @ApiModelProperty(value = "删除状态（0正常，1删除）")
    private Integer deleted;

    @ApiModelProperty(value = "父菜单标题", example = "父菜单标题")
    private String pTitle;
}
