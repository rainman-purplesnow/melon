package org.tieland.melon.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.tieland.melon.core.*;
import org.tieland.melon.gateway.support.GatewayGrayRequestContext;

/**
 * @author zhouxiang
 * @date 2020/3/19 10:48
 */
@Slf4j
public class DefaultMelonLoadBalancerClientFilter extends AbstractMelonLoadBalancerClientFilter {

    public DefaultMelonLoadBalancerClientFilter(LoadBalancerClient loadBalancer, LoadBalancerProperties properties,
                                                MelonService melonService, MelonConfig melonConfig) {
        super(loadBalancer, properties, melonService, melonConfig);
    }

    @Override
    protected GatewayGrayRequestContext getGatewayGrayRequestContext(ServerWebExchange exchange) {
        return new GatewayGrayRequestContext(exchange);
    }


}
