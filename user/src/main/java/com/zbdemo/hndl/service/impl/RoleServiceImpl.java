package com.zbdemo.hndl.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zbdemo.hndl.base.PageQuery;
import com.zbdemo.hndl.base.ResultData;
import com.zbdemo.hndl.enums.ResponseEnum;
import com.zbdemo.hndl.mapper.RoleMapper;
import com.zbdemo.hndl.service.MenuService;
import com.zbdemo.hndl.service.RoleService;
import com.zbdemo.hndl.user.dto.RoleMenuDto;
import com.zbdemo.hndl.user.entity.Role;
import com.zbdemo.hndl.user.vo.MenuTreeVo;
import com.zbdemo.hndl.user.vo.RoleMenuVo;
import com.zbdemo.hndl.utlis.EntityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhangbing
 * @title: RoleServiceImpl
 * @projectName hndl
 * @description: 角色接口实现
 * @date 2022/7/20上午12:11
 */
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private MenuService menuService;

    @Override
    public ResultData insertRole(Role role) {
        // 初始化ID，创建时间，修改时间等公共字段
        try {
            EntityUtil.initInsert(role);
        }catch (Exception e){
            log.info("insertRole 添加角色初始化错误:"+e.getMessage());
            return ResultData.createResult(ResponseEnum.ENTITY_INIT_ERROR,e.getMessage());
        }
        roleMapper.insertRole(role);
        return ResultData.success();
    }

    @Override
    public ResultData deleteRole(String id) {
        roleMapper.deleteRole(id);
        return ResultData.success();
    }

    @Override
    public ResultData updateRole(Role role) {
        // 初始化ID，创建时间，修改时间等公共字段
        try {
            EntityUtil.initUpdate(role);
        }catch (Exception e){
            log.info("updateRole 编辑角色初始化错误:"+e.getMessage());
            return ResultData.createResult(ResponseEnum.ENTITY_INIT_ERROR,e.getMessage());
        }
        roleMapper.updateRole(role);
        return ResultData.success();
    }

    @Override
    public ResultData<Role> findRoleById(String id) {
        Role role = roleMapper.findRoleById(id);
        return ResultData.success(role);
    }

    @Override
    public ResultData<Page<Role>> pageRole(PageQuery<Role> rolePageQuery) {
        //开始分页
        PageHelper.startPage(rolePageQuery.getPageNum(), rolePageQuery.getPageSize());
        //获取分页对象
        Page<Role> pageRole = roleMapper.pageRole(rolePageQuery.getParams());
        return ResultData.success(pageRole);
    }

    @Override
    public ResultData bindRoleMenu(RoleMenuDto roleMenuDto) {
        // 绑定前删除原有权限
        roleMapper.deleteRoleMenuByRoleId(roleMenuDto.getRoleId());
        // 绑定新的角色权限
        roleMapper.bindRoleMenu(roleMenuDto);
        return ResultData.success();
    }

    @Override
    public ResultData<RoleMenuVo> findRoleMenuById(String id) {
        RoleMenuVo roleMenuVo = new RoleMenuVo();
        Role role = roleMapper.findRoleById(id);
        roleMenuVo.setRole(role);
        List<MenuTreeVo> menuList = menuService.findMenuTreeVo(id);
        roleMenuVo.setMenus(menuList);
        return ResultData.success(roleMenuVo);
    }
}
