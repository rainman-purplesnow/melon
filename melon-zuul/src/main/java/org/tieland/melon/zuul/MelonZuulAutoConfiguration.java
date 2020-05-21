package org.tieland.melon.zuul;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tieland.melon.core.DefaultMelonService;
import org.tieland.melon.core.MelonConfig;
import org.tieland.melon.core.MelonService;
import org.tieland.melon.ribbon.DefaultMelonZoneAvoidanceRule;
import org.tieland.melon.supports.MelonSupportsAutoConfiguration;
import org.tieland.melon.zuul.filter.AbstractMelonZuulFilter;
import org.tieland.melon.zuul.filter.DefaultMelonZuulFilter;
import org.tieland.melon.zuul.filter.MelonZuulFilter;
import org.tieland.melon.zuul.web.MelonHystrixFilter;

/**
 * @author zhouxiang
 * @date 2020/3/17 14:46
 */
@Configuration
@EnableConfigurationProperties(value = {MelonConfig.class})
@ConditionalOnProperty(value = "config.melon.enabled", havingValue = "true")
@RibbonClients(defaultConfiguration = DefaultMelonZoneAvoidanceRule.class)
@AutoConfigureAfter(MelonSupportsAutoConfiguration.class)
public class MelonZuulAutoConfiguration {

    @Autowired
    public void configEureka(EurekaInstanceConfigBean eurekaInstanceConfigBean, MelonConfig melonConfig){
        if(melonConfig == null || StringUtils.isBlank(melonConfig.getGroup())){
            throw new IllegalArgumentException(" Melon config error.group is null or empty. ");
        }
        eurekaInstanceConfigBean.setAppGroupName(melonConfig.getGroup());
    }

    @Bean
    @ConditionalOnMissingBean
    public MelonHystrixFilter melonHystrixFilter(){
        return new MelonHystrixFilter();
    }

    @Bean
    @ConditionalOnMissingBean
    public MelonZuulFilter melonZuulFilter(MelonConfig melonConfig){
        return new DefaultMelonZuulFilter(melonService(), melonConfig);
    }

    @Bean
    @ConditionalOnMissingClass(value = {"org.springframework.data.redis.core.RedisTemplate",
            "org.springframework.data.redis.core.StringRedisTemplate"})
    @ConditionalOnMissingBean
    public MelonService melonService(){
        return new DefaultMelonService();
    }
}
