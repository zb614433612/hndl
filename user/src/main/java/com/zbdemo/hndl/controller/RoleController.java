package com.zbdemo.hndl.controller;

import com.github.pagehelper.Page;
import com.zbdemo.hndl.base.PageQuery;
import com.zbdemo.hndl.base.ResultData;
import com.zbdemo.hndl.groups.InsertGroup;
import com.zbdemo.hndl.groups.UpdateGroup;
import com.zbdemo.hndl.service.RoleService;
import com.zbdemo.hndl.user.dto.RoleMenuDto;
import com.zbdemo.hndl.user.entity.Role;
import com.zbdemo.hndl.user.vo.RoleMenuVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * @author zhangbing
 * @title: RoleController
 * @projectName hndl
 * @description: 权限管理
 * @date 2022/7/19下午11:08
 */
@RestController
@Api(value = "role", tags = "角色管理接口")
@RequestMapping("/role")
@Validated
public class RoleController {
    @Resource
    private RoleService roleService;

    @ApiOperation(value = "角色管理-添加角色")
    @PostMapping(value = "/insertRole")
    public ResultData insertRole(@RequestBody @Validated({InsertGroup.class}) Role role) {
        return roleService.insertRole(role);
    }

    @ApiOperation(value = "角色管理-删除角色")
    @GetMapping(value = "/deleteRole")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色id", required = true, dataType = "String", paramType = "query")
    })
    public ResultData deleteRole(@RequestParam("id") @NotNull(message = "角色id不能为空") String id) {
        return roleService.deleteRole(id);
    }

    @ApiOperation(value = "角色管理-编辑角色")
    @PostMapping(value = "/updateRole")
    public ResultData updateRole(@RequestBody @Validated({UpdateGroup.class}) Role role) {
        return roleService.updateRole(role);
    }

    @ApiOperation(value = "角色管理-根据角色ID查询角色信息")
    @GetMapping(value = "/findRoleById")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色id", required = true, dataType = "String", paramType = "query")
    })
    public ResultData<Role> findRoleById(@RequestParam("id") @NotNull(message = "角色id不能为空") String id) {
        return roleService.findRoleById(id);
    }

    @ApiOperation(value = "角色管理-分页条件查询角色")
    @PostMapping(value = "/pageRole")
    public ResultData<Page<Role>> pageRole(@RequestBody PageQuery<Role> rolePageQuery) {
        return roleService.pageRole(rolePageQuery);
    }

    @ApiOperation(value = "角色管理-绑定角色菜单")
    @PostMapping(value = "/bindRoleMenu")
    public ResultData bindRoleMenu(@RequestBody @Validated RoleMenuDto roleMenuDto) {
        return roleService.bindRoleMenu(roleMenuDto);
    }

    @ApiOperation(value = "角色管理-根据角色ID查询角色权限信息")
    @GetMapping(value = "/findRoleMenuById")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色id", required = true, dataType = "String", paramType = "query")
    })
    public ResultData<RoleMenuVo> findRoleMenuById(@RequestParam("id") @NotNull(message = "角色id不能为空") String id) {
        return roleService.findRoleMenuById(id);
    }
}
