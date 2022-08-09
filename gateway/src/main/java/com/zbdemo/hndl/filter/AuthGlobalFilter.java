package com.zbdemo.hndl.filter;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zbdemo.hndl.base.LoginVo;
import com.zbdemo.hndl.base.ResultData;
import com.zbdemo.hndl.enums.ResponseEnum;
import com.zbdemo.hndl.utlis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @author zhangbing
 * @title: AuthGlobalFilter
 * @projectName hndl
 * @description: 鉴权全局过滤器
 * @date 2022/7/21上午12:23
 */

@Component
@Slf4j
@RefreshScope
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    @Resource
    private RedisUtil redisUtil;
    /**
     * token 过期时间(秒)  2小时
     */
    @Value("${custom.tokenOutTime:7200}")
    private Integer tokenOutTime;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 执行后前置滤器 可以在请求被路由之前调用
        ServerHttpRequest request = exchange.getRequest();
        // 获取请求路径
        String url = request.getURI().getPath();
        // 不需要鉴权直接放行的请求
        String releaseUrl = "login,getLoginCode,v2";
        String[] releaseUrls = releaseUrl.split(",");
        for (String string:releaseUrls){
            if (url.indexOf(string) > 0) {
                Mono<Void> filter = chain.filter(exchange);
                // 执行后置过滤器 在路由调用后执行
                return filter;
            }
        }
        // 获取请求token
        String token = request.getHeaders().getFirst("token");
        if (StringUtils.isEmpty(token)) {
            log.info("AuthGlobalFilter 鉴权失败,缺少token参数。");
            return responseFailRs(exchange, ResultData.createResult(ResponseEnum.TOKEN_NULL_ERROR));
        }

        // 获取缓存
        Object LoginVoObject = redisUtil.get(token);
        if (LoginVoObject == null) {
            log.info("AuthGlobalFilter token已过期或者无效");
            return responseFailRs(exchange, ResultData.createResult(ResponseEnum.TOKEN_ERROR));
        }

        // 验证访问ip与登陆ip是否一致----------------------------------------------
        ObjectMapper objectMapper = new ObjectMapper();
        // Object转对象
        LoginVo loginVo = objectMapper.convertValue(LoginVoObject, LoginVo.class);
        // 获取请求ip地址
        String ipAddress = request.getRemoteAddress().getHostString();
        if (!ipAddress.equals(loginVo.getIpAddress())){
            log.info("AuthGlobalFilter 登陆ip地址与请求ip地址不一致，请重新登陆");
            redisUtil.del(token,loginVo.getId());
            return responseFailRs(exchange, ResultData.createResult(ResponseEnum.IP_ADDRESS_ERROR));
        }
        // 验证通过，刷新token时间------------------------------------------------
        redisUtil.expire(token,tokenOutTime);
        redisUtil.expire(loginVo.getId(),tokenOutTime);

        Mono<Void> filter = chain.filter(exchange);
        // 后置过滤器，在路由之后执行
        return filter;
    }

    private Mono<Void> responseFailRs(ServerWebExchange exchange,ResultData resultData) {
        ServerHttpResponse serverHttpResponse = exchange.getResponse();
        serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
        serverHttpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
        byte[] bytes = JSON.toJSONString(resultData).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return serverHttpResponse.writeWith(Mono.just(buffer));
    }
    // 顺序，数值越小，优先级越高
    @Override
    public int getOrder() {
        return 0;
    }
}
