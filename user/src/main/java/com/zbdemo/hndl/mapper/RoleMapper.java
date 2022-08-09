package com.zbdemo.hndl.mapper;

import com.github.pagehelper.Page;
import com.zbdemo.hndl.user.dto.RoleMenuDto;
import com.zbdemo.hndl.user.entity.Menu;
import com.zbdemo.hndl.user.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangbing
 * @title: RoleMapper
 * @projectName hndl
 * @description: 角色ORM映射
 * @date 2022/7/20上午12:14
 */
@Mapper
public interface RoleMapper {
    /**
    　* @description: 添加角色
    　* @param Role
    　* @return void
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/20 上午12:20
    　*/
    void insertRole(@Param("role") Role role);

    /**
    　* @description: 删除角色
    　* @param String
    　* @return void
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/20 上午12:21
    　*/
    void deleteRole(@Param("id") String id);

    /**
    　* @description: 编辑角色
    　* @param Role
    　* @return void
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/20 上午12:22
    　*/
    void updateRole(@Param("role") Role role);

    /**
    　* @description: 根据角色ID查询角色信息
    　* @param String
    　* @return Role
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/20 上午12:25
    　*/
    Role findRoleById(@Param("id") String id);

    /**
    　* @description: 分页条件查询角色
    　* @param Role
    　* @return Page
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/20 上午12:26
    　*/
    Page<Role> pageRole(@Param("params") Role params);

    /**
    　* @description: 删除原有权限
    　* @param String
    　* @return void
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/20 上午12:27
    　*/
    void deleteRoleMenuByRoleId(@Param("roleId") String roleId);

    /**
    　* @description: 绑定角色菜单
    　* @param RoleMenuDto
    　* @return void
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/20 上午12:27
    　*/
    void bindRoleMenu(@Param("roleMenuDto") RoleMenuDto roleMenuDto);

    /**
    　* @description: 根据角色ID查询菜单
    　* @param String
    　* @return List
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/20 上午12:29
    　*/
    List<Menu> findMenuByRoleId(@Param("id") String id);
}
