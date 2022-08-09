package com.zbdemo.hndl.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zhangbing
 * @title: MenuTreeVo
 * @projectName hndl
 * @description: 树结构菜单输出类
 * @date 2022/7/20上午12:50
 */
@Data
@ApiModel(value = "树结构菜单输出类", description = "这是个输入对象")
public class MenuTreeVo {
    @ApiModelProperty(value = "主键ID")
    private String id;
    @ApiModelProperty(value = "父菜单ID")
    private String pid;
    @ApiModelProperty(value = "菜单类型")
    private Integer type;
    @ApiModelProperty(value = "菜单标题")
    private String title;
    @ApiModelProperty(value = "菜单排序")
    private Integer menuSort;
    @ApiModelProperty(value = "菜单图标")
    private String icon;
    @ApiModelProperty(value = "菜单跳转路径")
    private String path;
    @ApiModelProperty(value = "是否外部跳转")
    private Integer frame;
    @ApiModelProperty(value = "菜单备注")
    private String remark;
    @ApiModelProperty(value = "创建时间")
    private Long createTime;
    @ApiModelProperty(value = "更新时间")
    private Long updateTime;
    @ApiModelProperty(value = "删除状态（0正常，1删除）")
    private Integer deleted;
    @ApiModelProperty(value = "子菜单集合")
    private List<MenuTreeVo> menuTreeVoList;
}
