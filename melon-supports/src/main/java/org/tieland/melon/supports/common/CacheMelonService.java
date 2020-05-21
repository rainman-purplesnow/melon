package org.tieland.melon.supports.common;

import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.AbstractScheduledService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.tieland.melon.core.MelonService;
import org.tieland.melon.core.MelonSettings;
import java.util.concurrent.TimeUnit;

/**
 * 基于redis定时读取更新Melon配置
 * @author zhouxiang
 * @date 2019/8/28 11:49
 */
@Slf4j
public class CacheMelonService implements MelonService, InitializingBean {

    /**
     * StringRedisTemplate
     */
    private StringRedisTemplate redisTemplate;

    /**
     * MelonCacheConfig
     */
    private MelonCacheConfig melonCacheConfig;

    /**
     * 全局Melon配置
     */
    private volatile MelonSettings melonSettings;

    public CacheMelonService(StringRedisTemplate redisTemplate, MelonCacheConfig melonCacheConfig){
        this.redisTemplate = redisTemplate;
        this.melonCacheConfig = melonCacheConfig;
    }

    /**
     * 初始化
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        RenewScheduledService renewScheduledService = new RenewScheduledService();
        renewScheduledService.startAsync().awaitRunning();
    }

    @Override
    public MelonSettings getSettings() {
        return melonSettings;
    }

    private synchronized void renew(MelonSettings melonSettings){
        log.info(" new melonSettings:{} ", melonSettings);
        this.melonSettings = melonSettings;
    }

    /**
     * 定时job从redis读取更新
     */
    private class RenewScheduledService extends AbstractScheduledService {

        @Override
        protected void runOneIteration() throws Exception {
            try{
                String melonSettingsCache = redisTemplate.opsForValue().get(melonCacheConfig.getKey());
                log.debug(" melonSettingsCache:{} ", melonSettingsCache);
                if(StringUtils.isBlank(melonSettingsCache)){
                    log.info(" melonSettingsCache from redis is null or empty. ");
                    renew(null);
                    return;
                }

                MelonSettings newMelonSettings = JSONObject.parseObject(melonSettingsCache, MelonSettings.class);
                renew(newMelonSettings);
            }catch (Exception e){
                log.error(" renew melonSettings from cache error. ", e);
            }
        }

        @Override
        protected Scheduler scheduler() {
            return Scheduler.newFixedRateSchedule(melonCacheConfig.getInitialDelay(), melonCacheConfig.getRenewInterval(), TimeUnit.SECONDS);
        }
    }

}
