package org.tieland.melon.supports;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.tieland.melon.core.MelonService;
import org.tieland.melon.supports.common.CacheMelonService;
import org.tieland.melon.supports.common.MelonCacheConfig;

/**
 * @author zhouxiang
 * @date 2020/3/19 10:08
 */
@Configuration
@EnableConfigurationProperties(value = {MelonCacheConfig.class})
public class MelonSupportsAutoConfiguration {

    @Bean
    public MelonService melonService(StringRedisTemplate redisTemplate, MelonCacheConfig melonCacheConfig){
        CacheMelonService cacheMelonService = new CacheMelonService(redisTemplate, melonCacheConfig);
        return cacheMelonService;
    }

}
