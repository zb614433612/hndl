package com.zbdemo.hndl.service;

import com.github.pagehelper.Page;
import com.zbdemo.hndl.base.PageQuery;
import com.zbdemo.hndl.base.ResultData;
import com.zbdemo.hndl.user.dto.RoleMenuDto;
import com.zbdemo.hndl.user.entity.Role;
import com.zbdemo.hndl.user.vo.RoleMenuVo;

/**
 * @author zhangbing
 * @title: RoleService
 * @projectName hndl
 * @description: 角色管理接口
 * @date 2022/7/19下午11:17
 */
public interface RoleService {
    /**
    　* @description: 添加角色
    　* @param Role
    　* @return ResultData
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/20 上午12:00
    　*/
    ResultData insertRole(Role role);

    /**
    　* @description: 删除角色
    　* @param String
    　* @return ResultData
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/20 上午12:01
    　*/
    ResultData deleteRole(String id);

    /**
    　* @description: 编辑角色
    　* @param Role
    　* @return ResultData
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/20 上午12:02
    　*/
    ResultData updateRole(Role role);

    /**
    　* @description: 根据角色ID查询角色信息
    　* @param String
    　* @return ResultData
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/20 上午12:05
    　*/
    ResultData<Role> findRoleById(String id);

    /**
    　* @description: 分页条件查询角色
    　* @param PageQuery
    　* @return ResultData
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/20 上午12:07
    　*/
    ResultData<Page<Role>> pageRole(PageQuery<Role> rolePageQuery);

    /**
     　* @description: 绑定角色菜单
     　* @param RoleMenuDto
     　* @return ResultData
     　* @throws
     　* @author zhangbing
     　* @date 2022/7/20 上午12:13
     　*/
    ResultData bindRoleMenu(RoleMenuDto roleMenuDto);

    /**
    　* @description: 根据角色ID查询角色权限信息
    　* @param String
    　* @return ResultData
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/20 上午12:08
    　*/
    ResultData<RoleMenuVo> findRoleMenuById(String id);
}
