package com.zbdemo.hndl.service;

import com.github.pagehelper.Page;
import com.zbdemo.hndl.base.PageQuery;
import com.zbdemo.hndl.base.ResultData;
import com.zbdemo.hndl.user.entity.Menu;
import com.zbdemo.hndl.user.vo.MenuTreeVo;

import java.util.List;

/**
 * @author zhangbing
 * @title: MenuService
 * @projectName hndl
 * @description: 菜单接口
 * @date 2022/7/19下午5:23
 */
public interface MenuService {
    /**
    　* @description: 新增菜单
    　* @param Menu
    　* @return ResultData
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/19 下午5:34
    　*/
    ResultData insertMenu(Menu menu);

    /**
    　* @description: 编辑菜单
    　* @param Menu
    　* @return ResultData
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/19 下午5:48
    　*/
    ResultData updateMenu(Menu menu);

    /**
    　* @description: 删除菜单
    　* @param String
    　* @return ResultData
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/19 下午5:50
    　*/
    ResultData deleteMenu(String id);

    /**
    　* @description: 根据ID查询菜单信息
    　* @param String
    　* @return ResultData
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/19 下午5:53
    　*/
    ResultData<Menu> findMenuById(String id);

    /**
    　* @description: 分页条件查询菜单
    　* @param PageQuery
    　* @return ResultData
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/19 下午5:59
    　*/
    ResultData<Page<Menu>> pageMenu(PageQuery<Menu> menuPageQuery);

    /**
     　* @description: 查询树结构菜单
     　* @param PageQuery
     　* @return ResultData
     　* @throws
     　* @author zhangbing
     　* @date 2022/7/19 下午5:59
     　*/
    List<MenuTreeVo> findMenuTreeVo(String roleId);
}
