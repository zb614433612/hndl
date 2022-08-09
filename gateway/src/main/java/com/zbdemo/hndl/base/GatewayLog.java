package com.zbdemo.hndl.base;

import lombok.Data;

/**
 * @author zhangbing
 * @title: GatewayLog
 * @projectName hndl
 * @description: 日志记录实体
 * @date 2022/7/25下午5:12
 */
@Data
public class GatewayLog {
    private String id;
    /**访问实例*/
    private String targetServer;
    /**请求路径*/
    private String requestPath;
    /**请求方法*/
    private String requestMethod;
    /**协议 */
    private String schema;
    /**请求体*/
    private String requestBody;
    /**响应体*/
    private String responseData;
    /**请求ip*/
    private String ipAddress;
    /**请求时间*/
    private Long requestTime;
    /**响应时间*/
    private Long responseTime;
    /**执行时间*/
    private Long executeTime;
}
