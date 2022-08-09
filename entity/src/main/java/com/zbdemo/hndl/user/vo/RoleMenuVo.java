package com.zbdemo.hndl.user.vo;

import com.zbdemo.hndl.user.entity.Menu;
import com.zbdemo.hndl.user.entity.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zhangbing
 * @title: RoleMenuVo
 * @projectName hndl
 * @description: 角色菜单输出类
 * @date 2022/7/20上午12:28
 */
@Data
@ApiModel(value = "角色菜单输出类", description = "这是个输出对象")
public class RoleMenuVo {
    @ApiModelProperty(value = "角色信息")
    private Role role;

    @ApiModelProperty(value = "权限数组")
    private List<MenuTreeVo> menus;
}
