package com.zbdemo.hndl.mapper;

import com.zbdemo.hndl.user.entity.GatewayLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zhangbing
 * @title: AccessLogMapper
 * @projectName hndl
 * @description: 日志记录
 * @date 2022/7/27上午12:14
 */
@Mapper
public interface AccessLogMapper {
    /**
    　* @description: 写日志
    　* @param GatewayLog
    　* @return void
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/27 上午12:15
    　*/
    void insertAccessLog(GatewayLog gatewayLog);
}
