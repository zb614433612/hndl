package com.zbdemo.hndl;

import com.zbdemo.hndl.rocketmq.LogMessage;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@MapperScan(basePackages = "com.zbdemo.hndl.mapper")
@EnableBinding(LogMessage.class)
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
