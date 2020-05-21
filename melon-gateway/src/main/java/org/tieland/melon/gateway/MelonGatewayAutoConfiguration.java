package org.tieland.melon.gateway;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerClientAutoConfiguration;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tieland.melon.core.DefaultMelonService;
import org.tieland.melon.core.MelonConfig;
import org.tieland.melon.core.MelonService;
import org.tieland.melon.gateway.filter.DefaultMelonLoadBalancerClientFilter;
import org.tieland.melon.ribbon.GatewayMelonZoneAvoidanceRule;
import org.tieland.melon.supports.MelonSupportsAutoConfiguration;

/**
 * @author zhouxiang
 * @date 2020/3/18 17:48
 */
@Configuration
@EnableConfigurationProperties(value = {MelonConfig.class})
@ConditionalOnProperty(value = "config.melon.enabled", havingValue = "true")
@RibbonClients(defaultConfiguration = GatewayMelonZoneAvoidanceRule.class)
@AutoConfigureBefore({GatewayLoadBalancerClientAutoConfiguration.class})
@AutoConfigureAfter({MelonSupportsAutoConfiguration.class})
public class MelonGatewayAutoConfiguration {

    @Autowired
    public void configEureka(EurekaInstanceConfigBean eurekaInstanceConfigBean, MelonConfig melonConfig){
        if(melonConfig == null || StringUtils.isBlank(melonConfig.getGroup())){
            throw new IllegalArgumentException(" Melon config error.group is null or empty. ");
        }

        eurekaInstanceConfigBean.setAppGroupName(melonConfig.getGroup());
    }

    @Bean
    @ConditionalOnMissingClass(value = {"org.springframework.data.redis.core.RedisTemplate",
            "org.springframework.data.redis.core.StringRedisTemplate"})
    @ConditionalOnMissingBean
    public MelonService melonService(){
        return new DefaultMelonService();
    }

    @Bean
    @ConditionalOnBean({LoadBalancerClient.class})
    @ConditionalOnMissingBean
    public LoadBalancerClientFilter loadBalancerClientFilter(LoadBalancerClient loadBalancer, LoadBalancerProperties properties,
                                                             MelonConfig melonConfig, MelonService melonService){
        return new DefaultMelonLoadBalancerClientFilter(loadBalancer, properties, melonService, melonConfig);
    }

}
