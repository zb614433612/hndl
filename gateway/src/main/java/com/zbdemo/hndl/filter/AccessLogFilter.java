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
 * @description: ????????????????????????????????????
 * @date 2022/7/25??????5:13
 */

@Slf4j
@Component
@RefreshScope
public class AccessLogFilter implements GlobalFilter, Ordered {
    @Resource
    LogMessage logMessage;
    @Resource
    RedisUtil redisUtil;
    // base64??????
    public static BASE64Encoder base64Encoder = new BASE64Encoder();
    // base64??????
    public static BASE64Decoder base64Decoder = new BASE64Decoder();

    private final List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();

    /**
     * ?????????????????????????????????????????????????????????????????????
     */
    private static final String EUREKA_SUB_PFIX = "CompositeDiscoveryClient_";

    /**
     * ??????????????????????????????
     */
    @Value("${custom.insertAccessLog:true}")
    private boolean insertAccessLog;
    /**
     * ????????????????????????
     */
    @Value("${custom.printAccessLog:true}")
    private boolean printAccessLog;

    /**
     * ???????????????<-1??????????????????NettyWriteResponseFilter?????????????????????????????????????????????????????????????????????
     * ??????????????????????????? -1 ?????????????????????????????????????????????
     * @return
     */
    @Override
    public int getOrder() {
        return -2;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // ??????request
        ServerHttpRequest request = exchange.getRequest();
        // ??????????????????
        String requestPath = request.getPath().pathWithinApplication().value();
        // ????????????
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        // ????????????ip??????
        String ipAddress = request.getRemoteAddress().getHostString();
        // ??????????????????
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

        //???????????????
        ServerHttpResponseDecorator decoratedResponse = recordResponseLog(exchange, accessLog);

        return chain.filter(exchange.mutate().response(decoratedResponse).build())
                .then(Mono.fromRunnable(() -> {
                    // ????????????
                    writeAccessLog(accessLog);
                }));
    }


    /**
     * ?????? request body ????????????????????????(request body????????????????????????????????????????????????????????????????????????????????????????????????request body??????)
     * ??????: org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory
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

        // ?????? BodyInserter ?????? body(????????????body), ?????? request body ??????????????????
        BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());
        // the new content type will be computed by bodyInserter
        // and then set in the request decorator
        headers.remove(HttpHeaders.CONTENT_LENGTH);
        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
        return bodyInserter.insert(outputMessage,new BodyInserterContext())
                .then(Mono.defer(() -> {
                    // ??????????????????
                    ServerHttpRequest decoratedRequest = requestDecorate(exchange, headers, outputMessage);
                    // ??????????????????
                    ServerHttpResponseDecorator decoratedResponse = recordResponseLog(exchange, gatewayLog);
                    // ???????????????
                    return chain.filter(exchange.mutate().request(decoratedRequest).response(decoratedResponse).build())
                            .then(Mono.fromRunnable(() -> {
                                // ????????????
                                writeAccessLog(gatewayLog);
                            }));
                }));
    }

    /**
     * ????????????
     * @author javadaily
     * @date 2021/3/24 14:53
     * @param gatewayLog ????????????
     */
    private void writeAccessLog(GatewayLog gatewayLog) {
        if (insertAccessLog){
            // ??????Spring Message??????
            Message<GatewayLog> logMessageObject = MessageBuilder.withPayload(gatewayLog).build();
            Boolean b = logMessage.logOutput().send(logMessageObject);
            // ?????????????????????????????????????????????????????????????????????????????????????????????????????????
            if (b && printAccessLog){
                log.info("????????????MQ?????????"+gatewayLog);
            }else if (!b){
                log.info("????????????MQ?????????"+gatewayLog);
            }
        }
    }

    /**
     * ?????????????????????????????? headers
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
     * ??????????????????
     * ?????? DataBufferFactory ????????????????????????????????????
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
                    // ??????????????????
                    Long executeTime = (responseTime - gatewayLog.getRequestTime());
                    gatewayLog.setExecuteTime(executeTime);
                    // ?????????????????????????????? json ?????????
                    String originalResponseContentType = exchange.getAttribute(ServerWebExchangeUtils.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR);
                    if (ObjectUtils.equals(this.getStatusCode(), HttpStatus.OK)
                            && StringUtils.isNotBlank(originalResponseContentType)
                            && originalResponseContentType.contains("application/json")) {
                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                        return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                            // ???????????????????????????????????????????????????
                            DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                            DataBuffer join = dataBufferFactory.join(dataBuffers);
                            byte[] content = new byte[join.readableByteCount()];
                            join.read(content);
                            // ???????????????
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
