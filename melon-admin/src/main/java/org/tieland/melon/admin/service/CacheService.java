package org.tieland.melon.admin.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.tieland.melon.admin.vo.MelonVO;

/**
 * @author zhouxiang
 * @date 2019/8/28 18:37
 */
@Component
public class CacheService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final String CACHE_KEY_PREFIX = "org:tieland:melon:";

    public void refresh(MelonVO melonVO){
        String cacheKey = CACHE_KEY_PREFIX+melonVO.getId();
        redisTemplate.opsForValue().set(cacheKey, JSONObject.toJSONString(melonVO));
    }

    public void evict(Long melonId){
        String cacheKey = CACHE_KEY_PREFIX+melonId;
        redisTemplate.opsForValue().set(cacheKey, null);
    }

}
