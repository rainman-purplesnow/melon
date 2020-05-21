package org.tieland.melon.zuul.support;

import com.netflix.zuul.context.RequestContext;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author zhouxiang
 * @date 2020/3/19 11:23
 */
public class RedisZuulGrayRequestContext extends ZuulGrayRequestContext {

    private RedisTemplate redisTemplate;

    public RedisZuulGrayRequestContext(RequestContext requestContext, RedisTemplate redisTemplate) {
        super(requestContext);
        this.redisTemplate = redisTemplate;
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }
}
