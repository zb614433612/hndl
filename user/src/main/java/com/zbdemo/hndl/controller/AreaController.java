package com.zbdemo.hndl.controller;

import com.zbdemo.hndl.base.ResultData;
import com.zbdemo.hndl.service.AreaService;
import com.zbdemo.hndl.user.entity.Area;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhangbing
 * @title: AreaController
 * @projectName hndl
 * @description: 区域相关接口
 * @date 2022/8/4上午12:03
 */
@RestController
@Api(value = "area", tags = "区域相关接口")
@RequestMapping("/area")
@Validated
public class AreaController {
    @Resource
    AreaService areaService;

    @ApiOperation(value = "获取区域信息(区域id为空时查询省级)")
    @GetMapping(value = "/findArea")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaId", value = "区域id", dataType = "Integer", paramType = "query")
    })
    public ResultData<List<Area>> findArea(@RequestParam Integer areaId) {
        return areaService.findArea(areaId);
    }
}
