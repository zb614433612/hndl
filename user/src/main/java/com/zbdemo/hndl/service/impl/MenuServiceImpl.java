package com.zbdemo.hndl.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zbdemo.hndl.base.PageQuery;
import com.zbdemo.hndl.base.ResultData;
import com.zbdemo.hndl.enums.ResponseEnum;
import com.zbdemo.hndl.mapper.MenuMapper;
import com.zbdemo.hndl.service.MenuService;
import com.zbdemo.hndl.user.entity.Menu;
import com.zbdemo.hndl.user.vo.MenuTreeVo;
import com.zbdemo.hndl.utlis.EntityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhangbing
 * @title: MenuServiceImpl
 * @projectName hndl
 * @description: 菜单接口实现
 * @date 2022/7/19下午5:26
 */
@Service
@Slf4j
public class MenuServiceImpl implements MenuService {
    @Resource
    MenuMapper menuMapper;

    @Override
    public ResultData insertMenu(Menu menu) {
        // 初始化ID，创建时间，修改时间等公共字段
        try {
            EntityUtil.initInsert(menu);
        }catch (Exception e){
            log.info("insertMenu 添加菜单初始化错误:"+e.getMessage());
            return ResultData.createResult(ResponseEnum.ENTITY_INIT_ERROR,e.getMessage());
        }
        menuMapper.insertMenu(menu);
        return ResultData.success();
    }

    @Override
    public ResultData updateMenu(Menu menu) {
        // 初始化ID，创建时间，修改时间等公共字段
        try {
            EntityUtil.initUpdate(menu);
        }catch (Exception e){
            log.info("updateMenu 修改菜单初始化错误:"+e.getMessage());
            return ResultData.createResult(ResponseEnum.ENTITY_INIT_ERROR,e.getMessage());
        }
        menuMapper.updateMenu(menu);
        return ResultData.success();
    }

    @Override
    public ResultData deleteMenu(String id) {
        menuMapper.deleteMenu(id);
        return ResultData.success();
    }

    @Override
    public ResultData<Menu> findMenuById(String id) {
        Menu menu = menuMapper.findMenuById(id);
        return ResultData.success(menu);
    }

    @Override
    public ResultData<Page<Menu>> pageMenu(PageQuery<Menu> menuPageQuery) {
        //开始分页
        PageHelper.startPage(menuPageQuery.getPageNum(), menuPageQuery.getPageSize());
        //获取分页对象
        Page<Menu> pageMenu = menuMapper.pageMenu(menuPageQuery.getParams());
        return ResultData.success(pageMenu);
    }

    @Override
    public List<MenuTreeVo> findMenuTreeVo(String roleId) {
        // 判断查询所有菜单还是根据角色查询菜单
        if (roleId == null){
            // 查询所有一级菜单
            List<MenuTreeVo> menuTreeVos = menuMapper.findMenuMax();
            // 递归查询
            recurveMenu(menuTreeVos,null);
            return menuTreeVos;
        }else {
            // 根据角色一级菜单
            List<MenuTreeVo> menuTreeVos = menuMapper.findMenuMaxByRoleId(roleId);
            // 递归查询
            recurveMenu(menuTreeVos,roleId);
            return menuTreeVos;
        }
    }

    private void recurveMenu(List<MenuTreeVo> menuTreeVos,String roleId){
        if (roleId == null){
            // 递归查询下级菜单（所有）
            for (MenuTreeVo menuTreeVo : menuTreeVos){
                // 根据父ID查询菜单列表
                List<MenuTreeVo> menuChildVos = menuMapper.findMenuChildByPid(menuTreeVo.getId());
                if (menuChildVos != null && menuChildVos.size() != 0){
                    recurveMenu(menuChildVos,null);
                }
                menuTreeVo.setMenuTreeVoList(menuChildVos);
            }
        }else {
            // 递归查询下级菜单(role权限)
            for (MenuTreeVo menuTreeVo : menuTreeVos){
                // 根据父ID查询菜单列表
                List<MenuTreeVo> menuChildVos = menuMapper.findMenuChildByPidAndRole(menuTreeVo.getId(),roleId);
                if (menuChildVos != null && menuChildVos.size() != 0){
                    recurveMenu(menuChildVos,roleId);
                }
                menuTreeVo.setMenuTreeVoList(menuChildVos);
            }
        }

    }
}
