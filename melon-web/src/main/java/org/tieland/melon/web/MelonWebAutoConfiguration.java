package org.tieland.melon.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tieland.melon.core.MelonConfig;
import org.tieland.melon.ribbon.DefaultMelonZoneAvoidanceRule;
import org.tieland.melon.web.feign.MelonRequestInterceptor;
import org.tieland.melon.web.filter.MelonContextFilter;

/**
 * @author zhouxiang
 * @date 2020/3/17 16:34
 */
@Configuration
@EnableConfigurationProperties(value = {MelonConfig.class})
@ConditionalOnProperty(value = "config.melon.enabled", havingValue = "true")
@RibbonClients(defaultConfiguration = DefaultMelonZoneAvoidanceRule.class)
public class MelonWebAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public MelonRequestInterceptor melonRequestInterceptor(){
        return new MelonRequestInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean
    public MelonContextFilter melonContextFilter(MelonConfig melonConfig){
        return new MelonContextFilter(melonConfig);
    }

    @Autowired
    public void configEureka(EurekaInstanceConfigBean eurekaInstanceConfigBean, MelonConfig melonConfig){
        if(melonConfig == null || StringUtils.isBlank(melonConfig.getGroup())){
            throw new IllegalArgumentException(" Melon config error.group is null or empty. ");
        }

        eurekaInstanceConfigBean.setAppGroupName(melonConfig.getGroup());
    }

}
