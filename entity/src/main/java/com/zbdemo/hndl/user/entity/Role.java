package com.zbdemo.hndl.user.entity;

import com.zbdemo.hndl.groups.InsertGroup;
import com.zbdemo.hndl.groups.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zhangbing
 * @title: Role
 * @projectName hndl
 * @description: 角色
 * @date 2022/7/19下午11:56
 */
@Data
@ApiModel(value = "角色管理实体类", description = "这是个实体对象")
public class Role {
    @ApiModelProperty(value = "主键ID")
    @NotNull(groups = {UpdateGroup.class},message = "id不能为空")
    private String id;
    @ApiModelProperty(value = "角色名称", example = "角色名称", required = true)
    @NotNull(message = "角色名称不能为空",groups = {InsertGroup.class})
    private String roleName;
    @ApiModelProperty(value = "角色备注", example = "角色备注", required = true)
    private String remake;
    @ApiModelProperty(value = "创建时间")
    private Long createTime;
    @ApiModelProperty(value = "更新时间")
    private Long updateTime;
    @ApiModelProperty(value = "删除状态（0正常，1删除）")
    private Integer deleted;
}
