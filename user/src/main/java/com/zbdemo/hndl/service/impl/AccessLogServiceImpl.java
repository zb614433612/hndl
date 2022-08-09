package com.zbdemo.hndl.service.impl;

import com.zbdemo.hndl.mapper.AccessLogMapper;
import com.zbdemo.hndl.service.AccessLogService;
import com.zbdemo.hndl.user.entity.GatewayLog;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author zhangbing
 * @title: AccessLogServiceImpl
 * @projectName hndl
 * @description: 日志记录实现
 * @date 2022/7/27上午12:13
 */
@Service
public class AccessLogServiceImpl implements AccessLogService {
    @Resource
    AccessLogMapper accessLogMapper;

    @Override
    public void insertAccessLog(GatewayLog gatewayLog) {
        UUID uuid = UUID.randomUUID();
        String uu = uuid.toString().replaceAll("-", "");
        gatewayLog.setId(uu);
        accessLogMapper.insertAccessLog(gatewayLog);
    }
}
