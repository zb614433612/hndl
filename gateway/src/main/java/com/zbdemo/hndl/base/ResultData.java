package com.zbdemo.hndl.base;


import com.zbdemo.hndl.enums.ResponseEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangbing
 * @ClassName: PageInfo
 * @Description: 查询返回实体封装
 * @date 2020/6/24 14:09
 * @Copyright
 */
@ApiModel(value = "分页查询对象")
@Data
public class ResultData<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /*
        返回状态码
     */
    @ApiModelProperty(value = "状态码")
    private String code;

    /*
        返回消息
     */
    @ApiModelProperty(value = "消息")
    private String msg;

    /*
        是否加密（false未加密，true已加密）
     */
    @ApiModelProperty(value = "是否加密")
    private Boolean encryption = false;

    /*
        返回数据
     */
    @ApiModelProperty(value = "数据体")
    private T data;


    private ResultData() {

    }

    private ResultData(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private ResultData(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    /**
     * 创建一个返回对象，带参数对对象
     *
     * @return ResultData
     */
    public static <T> ResultData<T> createResult(ResponseEnum responseEnum, T data) {
        ResultData<T> result = new ResultData<T>();
        String code = responseEnum.getCode() + "";
        String msg = responseEnum.getMsg();
        if (code != null && data == null) {
            result.setCode(code);
            result.setMsg(msg);
            return result;
        } else if (data != null) {
            result.setCode(code);
            result.setMsg(msg);
            result.setData(data);
            return result;
        } else {
            return result;
        }
    }

    /**
     * 创建一个自定义msg且不带参数的返回对象
     *
     * @return ResultData
     */
    public static <T> ResultData<T> createResult(ResponseEnum responseEnum, String msg) {
        ResultData<T> result = new ResultData<T>();
        String code = responseEnum.getCode() + "";
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    /**
     * 创建一个不带数据的返回对象
     *
     * @return ResultData
     */
    public static <T> ResultData<T> createResult(ResponseEnum responseEnum) {
        ResultData<T> result = new ResultData<T>();
        String code = responseEnum.getCode() + "";
        String msg = responseEnum.getMsg();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    /**
     * @return
     * @Description 创建一个成功对象带参数
     * @Param
     * @Author zhangbing
     * @Date 2020/6/29 11:28
     */
    public static <T> ResultData<T> success(T data) {
        return createResult(ResponseEnum.SUCCESS_CODE, data);
    }

    /**
     * @return
     * @Description 创建一个成功对象不带参数
     * @Param
     * @Author zhangbing
     * @Date 2020/6/29 11:28
     */
    public static <T> ResultData<T> success() {
        return createResult(ResponseEnum.SUCCESS_CODE);
    }
}
