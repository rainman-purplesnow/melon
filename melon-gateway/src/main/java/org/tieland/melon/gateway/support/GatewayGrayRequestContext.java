package org.tieland.melon.gateway.support;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.tieland.melon.core.GrayRequestContext;

/**
 * 上下文
 * @author zhouxiang
 * @date 2019/10/12 13:55
 */
public class GatewayGrayRequestContext implements GrayRequestContext {

    private ServerWebExchange exchange;

    public GatewayGrayRequestContext(ServerWebExchange exchange){
        this.exchange = exchange;
    }

    public ServerWebExchange getExchange() {
        return exchange;
    }
}
