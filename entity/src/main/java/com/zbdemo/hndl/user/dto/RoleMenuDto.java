package com.zbdemo.hndl.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author zhangbing
 * @title: RoleMenuDto
 * @projectName hndl
 * @description: 角色菜单绑定输入类
 * @date 2022/7/20上午12:12
 */
@Data
@ApiModel(value = "角色菜单绑定输入类", description = "这是个输入对象")
public class RoleMenuDto {
    @ApiModelProperty(value = "角色ID")
    @NotNull(message = "角色id不能为空")
    private String roleId;

    @ApiModelProperty(value = "菜单ID数组")
    private List<String> menuIds;
}
