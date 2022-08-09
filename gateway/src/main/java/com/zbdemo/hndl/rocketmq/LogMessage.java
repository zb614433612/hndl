package com.zbdemo.hndl.rocketmq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

/**
 * @author zhangbing
 * @title: LogMessge
 * @projectName hndl
 * @description: MQ生产者接口
 * @date 2022/8/6上午12:02
 */
public interface LogMessage {
    @Output("log-output")
    MessageChannel logOutput();
}
