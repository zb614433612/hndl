package com.zbdemo.hndl.filter;

import com.zbdemo.hndl.base.GatewayLog;
import com.zbdemo.hndl.rocketmq.LogMessage;
import com.zbdemo.hndl.utlis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @author zhangbing
 * @title: AccessLogFilter
 * @projectName hndl
 * @description: 访问日志及响应处理过滤器
 * @date 2022/7/25下午5:13
 */

@Slf4j
@Component
@RefreshScope
public class AccessLogFilter implements GlobalFilter, Ordered {
    @Resource
    LogMessage logMessage;
    @Resource
    RedisUtil redisUtil;
    // base64加密
    public static BASE64Encoder base64Encoder = new BASE64Encoder();
    // base64解密
    public static BASE64Decoder base64Decoder = new BASE64Decoder();

    private final List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();

    /**
     * 发现功能的方法的名字，注册的服务会加入这个前缀
     */
    private static final String EUREKA_SUB_PFIX = "CompositeDiscoveryClient_";

    /**
     * 是否记录日志访问日志
     */
    @Value("${custom.insertAccessLog:true}")
    private boolean insertAccessLog;
    /**
     * 是否打印访问日志
     */
    @Value("${custom.printAccessLog:true}")
    private boolean printAccessLog;

    /**
     * 顺序必须是<-1，否则标准的NettyWriteResponseFilter将在您的过滤器得到一个被调用的机会之前发送响应
     * 也就是说如果不小于 -1 ，将不会执行获取后端响应的逻辑
     * @return
     */
    @Override
    public int getOrder() {
        return -2;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取request
        ServerHttpRequest request = exchange.getRequest();
        // 获取请求路径
        String requestPath = request.getPath().pathWithinApplication().value();
        // 获取路由
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        // 获取请求ip地址
        String ipAddress = request.getRemoteAddress().getHostString();
        // 封装日志对象
        GatewayLog gatewayLog = new GatewayLog();
        gatewayLog.setSchema(request.getURI().getScheme());
        gatewayLog.setRequestMethod(request.getMethodValue());
        gatewayLog.setRequestPath(requestPath);
        gatewayLog.setTargetServer(route.getId().substring(EUREKA_SUB_PFIX.length()));
        gatewayLog.setRequestTime(System.currentTimeMillis()/1000);
        gatewayLog.setIpAddress(ipAddress);

        MediaType mediaType = request.getHeaders().getContentType();

        if(MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType) || MediaType.APPLICATION_JSON.isCompatibleWith(mediaType)){
            return writeBodyLog(exchange, chain, gatewayLog);
        }else{
            return writeBasicLog(exchange, chain, gatewayLog);
        }
    }

    private Mono<Void> writeBasicLog(ServerWebExchange exchange, GatewayFilterChain chain, GatewayLog accessLog) {
        StringBuilder builder = new StringBuilder();
        MultiValueMap<String, String> queryParams = exchange.getRequest().getQueryParams();
        for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
            builder.append(entry.getKey()).append("=").append(StringUtils.join(entry.getValue(), ","));
        }
        accessLog.setRequestBody(builder.toString());

        //获取响应体
        ServerHttpResponseDecorator decoratedResponse = recordResponseLog(exchange, accessLog);

        return chain.filter(exchange.mutate().response(decoratedResponse).build())
                .then(Mono.fromRunnable(() -> {
                    // 打印日志
                    writeAccessLog(accessLog);
                }));
    }


    /**
     * 解决 request body 只能读取一次问题(request body是消费类型，读取一次后失效，也就是说在过滤器读取过后需要重新封装request body参数)
     * 参考: org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory
     * @param exchange
     * @param chain
     * @param gatewayLog
     * @return
     */
    private Mono writeBodyLog(ServerWebExchange exchange, GatewayFilterChain chain, GatewayLog gatewayLog) {
        ServerRequest serverRequest = ServerRequest.create(exchange,messageReaders);
        Mono<String> modifiedBody = serverRequest.bodyToMono(String.class)
                .flatMap(body ->{
                    gatewayLog.setRequestBody(body.replaceAll(" ", ""));
                    return Mono.just(body);
                });

        // 通过 BodyInserter 插入 body(支持修改body), 避免 request body 只能获取一次
        BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());
        // the new content type will be computed by bodyInserter
        // and then set in the request decorator
        headers.remove(HttpHeaders.CONTENT_LENGTH);
        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
        return bodyInserter.insert(outputMessage,new BodyInserterContext())
                .then(Mono.defer(() -> {
                    // 重新封装请求
                    ServerHttpRequest decoratedRequest = requestDecorate(exchange, headers, outputMessage);
                    // 记录响应日志
                    ServerHttpResponseDecorator decoratedResponse = recordResponseLog(exchange, gatewayLog);
                    // 记录普通的
                    return chain.filter(exchange.mutate().request(decoratedRequest).response(decoratedResponse).build())
                            .then(Mono.fromRunnable(() -> {
                                // 打印日志
                                writeAccessLog(gatewayLog);
                            }));
                }));
    }

    /**
     * 打印日志
     * @author javadaily
     * @date 2021/3/24 14:53
     * @param gatewayLog 网关日志
     */
    private void writeAccessLog(GatewayLog gatewayLog) {
        if (insertAccessLog){
            // 创建Spring Message对象
            Message<GatewayLog> logMessageObject = MessageBuilder.withPayload(gatewayLog).build();
            Boolean b = logMessage.logOutput().send(logMessageObject);
            // 如果发送成功，根据配置决定是否打印到控制台！发送失败则直接打印到控制台
            if (b && printAccessLog){
                log.info("日志发送MQ成功："+gatewayLog);
            }else if (!b){
                log.info("日志发送MQ失败："+gatewayLog);
            }
        }
    }

    /**
     * 请求装饰器，重新计算 headers
     * @param exchange
     * @param headers
     * @param outputMessage
     * @return
     */
    private ServerHttpRequestDecorator requestDecorate(ServerWebExchange exchange, HttpHeaders headers,
                                                       CachedBodyOutputMessage outputMessage) {
        return new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public HttpHeaders getHeaders() {
                long contentLength = headers.getContentLength();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.putAll(super.getHeaders());
                if (contentLength > 0) {
                    httpHeaders.setContentLength(contentLength);
                } else {
                    // TODO: this causes a 'HTTP/1.1 411 Length Required' // on
                    // httpbin.org
                    httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                }
                return httpHeaders;
            }

            @Override
            public Flux<DataBuffer> getBody() {
                return outputMessage.getBody();
            }
        };
    }


    /**
     * 记录响应日志
     * 通过 DataBufferFactory 解决响应体分段传输问题。
     */
    private ServerHttpResponseDecorator recordResponseLog(ServerWebExchange exchange, GatewayLog gatewayLog) {
        ServerHttpResponse response = exchange.getResponse();
        DataBufferFactory bufferFactory = response.bufferFactory();

        return new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Long responseTime = System.currentTimeMillis()/1000;
                    gatewayLog.setResponseTime(responseTime);
                    // 计算执行时间
                    Long executeTime = (responseTime - gatewayLog.getRequestTime());
                    gatewayLog.setExecuteTime(executeTime);
                    // 获取响应类型，如果是 json 就打印
                    String originalResponseContentType = exchange.getAttribute(ServerWebExchangeUtils.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR);
                    if (ObjectUtils.equals(this.getStatusCode(), HttpStatus.OK)
                            && StringUtils.isNotBlank(originalResponseContentType)
                            && originalResponseContentType.contains("application/json")) {
                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                        return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                            // 合并多个流集合，解决返回体分段传输
                            DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                            DataBuffer join = dataBufferFactory.join(dataBuffers);
                            byte[] content = new byte[join.readableByteCount()];
                            join.read(content);
                            // 释放掉内存
                            DataBufferUtils.release(join);
                            String responseResult = new String(content, StandardCharsets.UTF_8);
                            gatewayLog.setResponseData(responseResult);
                            return bufferFactory.wrap(content);
                        }));
                    }
                }
                // if body is not a flux. never got there.
                return super.writeWith(body);
            }
        };
    }
}
