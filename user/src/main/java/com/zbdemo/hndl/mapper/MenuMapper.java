package com.zbdemo.hndl.mapper;

import com.github.pagehelper.Page;
import com.zbdemo.hndl.user.entity.Menu;
import com.zbdemo.hndl.user.vo.MenuTreeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangbing
 * @title: MenuMapper
 * @projectName hndl
 * @description: 菜单管理ORM
 * @date 2022/7/19下午5:37
 */
@Mapper
public interface MenuMapper {
    /**
    　* @description: 新增菜单
    　* @param Menu
    　* @return int
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/19 下午5:38
    　*/
    void insertMenu(@Param("menu") Menu menu);

    /**
    　* @description: 编辑菜单
    　* @param Menu
    　* @return void
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/19 下午5:49
    　*/
    void updateMenu(@Param("menu") Menu menu);

    /**
    　* @description: 删除菜单
    　* @param String
    　* @return void
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/19 下午5:54
    　*/
    void deleteMenu(@Param("id") String id);

    /**
    　* @description: 根据菜单ID查询菜单信息
    　* @param String
    　* @return Menu
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/19 下午5:54
    　*/
    Menu findMenuById(@Param("id") String id);

    /**
    　* @description: 分页条件查询菜单
    　* @param Menu
    　* @return Page
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/19 下午6:01
    　*/
    Page<Menu> pageMenu(@Param("params") Menu params);

    /**
    　* @description: 查询所有一级菜单
    　* @param
    　* @return List
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/20 上午12:54
    　*/
    List<MenuTreeVo> findMenuMax();

    /**
    　* @description: 根据角色一级菜单
    　* @param String
    　* @return List
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/20 上午1:00
    　*/
    List<MenuTreeVo> findMenuMaxByRoleId(String roleId);

    /**
    　* @description: 根据父id查询子菜单
    　* @param String
    　* @return List
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/20 下午5:30
    　*/
    List<MenuTreeVo> findMenuChildByPid(String id);

    /**
    　* @description: 根据父id查询子菜单（role权限）
    　* @param String
    　* @return List
    　* @throws
    　* @author zhangbing
    　* @date 2022/8/4 下午7:39
    　*/
    List<MenuTreeVo> findMenuChildByPidAndRole(@Param("id") String id,@Param("roleId") String roleId);
}
