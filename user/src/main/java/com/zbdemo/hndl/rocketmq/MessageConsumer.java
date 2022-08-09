package com.zbdemo.hndl.rocketmq;

import com.zbdemo.hndl.service.AccessLogService;
import com.zbdemo.hndl.user.entity.GatewayLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author zhangbing
 * @title: MessageConsumer
 * @projectName hndl
 * @description: MQ消费者
 * @date 2022/8/6上午12:33
 */
@Component
@Slf4j
public class MessageConsumer {
    @Resource
    AccessLogService accessLogService;
    // 使用@Payload反序列化
    @StreamListener("log-input")
    public void onMessage(@Payload GatewayLog gatewayLog){
        accessLogService.insertAccessLog(gatewayLog);
        log.info("日志消息接收成功:"+gatewayLog);
    }
}
