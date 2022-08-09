package com.zbdemo.hndl.rocketmq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author zhangbing
 * @title: MessageSource
 * @projectName hndl
 * @description: MQ消费者自定义接口
 * @date 2022/8/6上午12:27
 */
public interface MessageSource {
    @Input("log-input")
    SubscribableChannel logInput();
}
