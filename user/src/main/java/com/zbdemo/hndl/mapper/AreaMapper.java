package com.zbdemo.hndl.mapper;

import com.zbdemo.hndl.user.entity.Area;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author zhangbing
 * @title: AreaMapper
 * @projectName hndl
 * @description: 区域信息ORM
 * @date 2022/8/4上午12:58
 */
@Mapper
public interface AreaMapper {
    /**
    　* @description: 获取区域信息(区域id为空时查询省级)
    　* @param String
    　* @return List
    　* @throws
    　* @author zhangbing
    　* @date 2022/8/4 上午12:59
    　*/
    List<Area> findArea(Integer areaId);
}
