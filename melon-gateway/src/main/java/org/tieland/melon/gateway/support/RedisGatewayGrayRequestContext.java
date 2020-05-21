package org.tieland.melon.gateway.support;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.tieland.melon.core.GrayRequestContext;

/**
 * @author zhouxiang
 * @date 2020/3/19 9:07
 */
public class RedisGatewayGrayRequestContext extends GatewayGrayRequestContext {

    private RedisTemplate redisTemplate;

    public RedisGatewayGrayRequestContext(ServerWebExchange exchange, RedisTemplate redisTemplate) {
        super(exchange);
        this.redisTemplate = redisTemplate;
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

}
