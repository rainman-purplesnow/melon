package org.tieland.melon.gateway.support;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.tieland.melon.core.AbstractGrayRule;
import org.tieland.melon.core.HeaderGrayCondition;

/**
 * @author zhouxiang
 * @date 2019/10/12 16:52
 */
@Slf4j
public class GatewayHeaderGrayRule extends AbstractGrayRule<GatewayGrayRequestContext, HeaderGrayCondition> {

    private static final String GATEWAY_HEADER_GRAY_RULE = "header-gray-rule";

    @Override
    protected boolean doApply(GatewayGrayRequestContext gatewayGrayRequestContext, HeaderGrayCondition headerGrayCondition) {
        ServerHttpRequest request = gatewayGrayRequestContext.getExchange().getRequest();
        String value = request.getHeaders().getFirst(headerGrayCondition.getKey());
        log.debug(" condition header key:{} value:{},  request value:{} ",
                headerGrayCondition.getKey(), headerGrayCondition.getValue(), value);
        if(StringUtils.isNotBlank(value) && headerGrayCondition.getValue().equals(value)){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public String name() {
        return GATEWAY_HEADER_GRAY_RULE;
    }
}
