package com.zbdemo.hndl.service;

import com.zbdemo.hndl.base.ResultData;
import com.zbdemo.hndl.user.entity.Area;

import java.util.List;

/**
 * @author zhangbing
 * @title: AreaService
 * @projectName hndl
 * @description: 区域服务类
 * @date 2022/8/4上午12:33
 */
public interface AreaService {
    /**
    　* @description: 获取区域信息(区域id为空时查询省级)
    　* @param String
    　* @return ResultData
    　* @throws
    　* @author zhangbing
    　* @date 2022/8/4 上午12:34
    　*/
    ResultData<List<Area>> findArea(Integer areaId);
}
