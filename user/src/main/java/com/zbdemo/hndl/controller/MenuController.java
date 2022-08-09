package com.zbdemo.hndl.controller;

import com.github.pagehelper.Page;
import com.zbdemo.hndl.base.PageQuery;
import com.zbdemo.hndl.base.ResultData;
import com.zbdemo.hndl.groups.InsertGroup;
import com.zbdemo.hndl.groups.UpdateGroup;
import com.zbdemo.hndl.service.MenuService;
import com.zbdemo.hndl.user.entity.Menu;
import com.zbdemo.hndl.user.vo.MenuTreeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author zhangbing
 * @title: MenuController
 * @projectName hndl
 * @description:
 * @date 2022/7/19上午1:14
 */
@RestController
@Api(value = "menu", tags = "菜单管理接口")
@RequestMapping("/menu")
@Validated
public class MenuController {
    @Resource
    private MenuService menuService;

    @ApiOperation(value = "菜单管理-添加菜单")
    @PostMapping(value = "/insertMenu")
    public ResultData insertMenu(@RequestBody @Validated({InsertGroup.class}) Menu menu) {
        return menuService.insertMenu(menu);
    }

    @ApiOperation(value = "菜单管理-编辑菜单")
    @PostMapping(value = "/updateMenu")
    public ResultData updateMenu(@RequestBody @Validated({UpdateGroup.class}) Menu menu) {
        return menuService.updateMenu(menu);
    }

    @ApiOperation(value = "菜单管理-删除菜单")
    @GetMapping(value = "/deleteMenu")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "菜单id", required = true, dataType = "String", paramType = "query")
    })
    public ResultData deleteMenu(@RequestParam("id") @NotNull(message = "菜单id不能为空") String id) {
        return menuService.deleteMenu(id);
    }

    @ApiOperation(value = "菜单管理-根据菜单ID查询菜单信息")
    @GetMapping(value = "/findMenuById")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "菜单id", required = true, dataType = "String", paramType = "query")
    })
    public ResultData<Menu> findMenuById(@RequestParam("id") @NotNull(message = "菜单id不能为空") String id) {
        return menuService.findMenuById(id);
    }

    @ApiOperation(value = "菜单管理-分页条件查询菜单")
    @PostMapping(value = "/pageMenu")
    public ResultData<Page<Menu>> pageMenu(@RequestBody PageQuery<Menu> menuPageQuery) {
        return menuService.pageMenu(menuPageQuery);
    }

    @ApiOperation(value = "菜单管理-查询完整菜单树")
    @PostMapping(value = "/findMenuTreeVo")
    public ResultData<List<MenuTreeVo>> findMenuTreeVo() {
        return ResultData.success(menuService.findMenuTreeVo(null));
    }
}
