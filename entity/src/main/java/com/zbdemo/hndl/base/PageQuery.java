package com.zbdemo.hndl.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author zhangbing
 * @title: PageDto
 * @projectName hndl
 * @description: 分页查询对象
 * @date 2022/7/18下午11:49
 */
@ApiModel(value = "分页查询对象")
@Data
public class PageQuery<T>  implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "查询条件对象")
    private T params;

    @ApiModelProperty(value = "当前页数",hidden = true)
    @NotNull(message = "当前页数不能为空")
    private Integer pageNum;

    @ApiModelProperty(value = "每页条数",hidden = true)
    @NotNull(message = "每页条数不能为空")
    private Integer pageSize;
}
