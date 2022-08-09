package com.zbdemo.hndl.user.entity;

import com.zbdemo.hndl.groups.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zhangbing
 * @title: Area
 * @projectName hndl
 * @description: 区域实体
 * @date 2022/8/4上午12:44
 */
@Data
@ApiModel(value = "区域实体对象", description = "这是个实体对象")
public class Area {
    @ApiModelProperty(value = "区域ID")
    private Integer areaId;
    @ApiModelProperty(value = "区域名称")
    private String name;
    @ApiModelProperty(value = "经度")
    private String lng;
    @ApiModelProperty(value = "纬度")
    private String lat;
    @ApiModelProperty(value = "父区域ID")
    private Integer parentId;
    @ApiModelProperty(value = "区域等级（0代表省级，1代表市级，2代表区县）")
    private Integer level;

}
