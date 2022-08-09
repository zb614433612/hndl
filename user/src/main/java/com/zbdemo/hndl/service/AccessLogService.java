package com.zbdemo.hndl.service;

import com.zbdemo.hndl.user.entity.GatewayLog;

/**
 * @author zhangbing
 * @title: AccessLogService
 * @projectName hndl
 * @description: 日志记录
 * @date 2022/7/27上午12:11
 */
public interface AccessLogService {
    /**
    　* @description: 写日志
    　* @param GatewayLog
    　* @return void
    　* @throws
    　* @author zhangbing
    　* @date 2022/7/27 上午12:12
    　*/
    void insertAccessLog(GatewayLog gatewayLog);
}
