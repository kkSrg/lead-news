package com.shawn.gateway.filter;

import com.heima.utils.common.AppJwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

/**
 * @author shawn
 * @date 2023年 01月 06日 14:33
 */
@Slf4j
@Component
public class NewsFilter implements GlobalFilter , Ordered {

    @Value("${exclude.urls}")
    private ArrayList<String> excludeUrls;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取访问路径
        String uri = exchange.getRequest().getURI().getPath();
        //获取响应对象
        ServerHttpResponse response = exchange.getResponse();
        log.info("拦截到路径:{}",uri);
        if (excludeUrls.contains(uri)){
            return chain.filter(exchange);
        }
        //获取token
        String token = exchange.getRequest().getHeaders().getFirst("token");
        if (StringUtils.isEmpty(token)){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        //解析token
        Claims claims = AppJwtUtil.getClaimsBody(token);
        int count = AppJwtUtil.verifyToken(claims);
        if (count==1||count==2){
            //token已失效
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
