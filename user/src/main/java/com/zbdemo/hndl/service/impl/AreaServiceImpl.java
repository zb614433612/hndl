package com.zbdemo.hndl.service.impl;

import com.zbdemo.hndl.base.ResultData;
import com.zbdemo.hndl.mapper.AreaMapper;
import com.zbdemo.hndl.service.AreaService;
import com.zbdemo.hndl.user.entity.Area;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhangbing
 * @title: AreaServiceImpl
 * @projectName hndl
 * @description: 区域实现类
 * @date 2022/8/4上午12:34
 */
@Service
public class AreaServiceImpl implements AreaService {
    @Resource
    AreaMapper areaMapper;

    @Override
    public ResultData<List<Area>> findArea(Integer areaId) {
        List<Area> areaList = areaMapper.findArea(areaId);
        return ResultData.success(areaList);
    }
}
