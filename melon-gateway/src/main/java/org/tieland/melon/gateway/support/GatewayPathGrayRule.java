package org.tieland.melon.gateway.support;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.MultiValueMap;
import org.tieland.melon.core.AbstractGrayRule;
import org.tieland.melon.core.PathGrayCondition;
import java.net.URI;
import java.util.Map;
import java.util.Set;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR;

/**
 * @author zhouxiang
 * @date 2019/10/12 16:52
 */
@Slf4j
public class GatewayPathGrayRule extends AbstractGrayRule<GatewayGrayRequestContext, PathGrayCondition> {

    private static final String GATEWAY_PATH_GRAY_RULE = "path-gray-rule";

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean doApply(GatewayGrayRequestContext gatewayGrayRequestContext, PathGrayCondition pathGrayCondition) {
        ServerHttpRequest request = gatewayGrayRequestContext.getExchange().getRequest();
        String requestUri = getRequestUri(gatewayGrayRequestContext);

        log.debug(" request uri:{}, condition uri:{}", requestUri, pathGrayCondition.getUri());
        if(!pathMatcher.match(pathGrayCondition.getUri(), requestUri)){
            log.debug(" uri not match ");
            return Boolean.FALSE;
        }

        if(StringUtils.isNotBlank(pathGrayCondition.getMethod())){
            String requestMethod = request.getMethod().name();
            log.debug("request method:{}; condition method:{}", requestMethod, pathGrayCondition.getMethod());
            if(!pathGrayCondition.getMethod().equalsIgnoreCase(requestMethod)){
                log.debug(" method not match ");
                return Boolean.FALSE;
            }
        }

        if(MapUtils.isNotEmpty(pathGrayCondition.getValues())){
            MultiValueMap<String, String> requestParams = request.getQueryParams();
            for(Map.Entry<String, String> entry : pathGrayCondition.getValues().entrySet()){
                String requestValue = requestParams.getFirst(entry.getKey());
                if(!entry.getValue().equals(requestValue)){
                    log.debug(" parameter not match ");
                    return Boolean.FALSE;
                }
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public String name() {
        return GATEWAY_PATH_GRAY_RULE;
    }

    //FIXME 待优化
    private String getRequestUri(GatewayGrayRequestContext gatewayGrayRequestContext){
        Set<URI> uris = gatewayGrayRequestContext.getExchange()
                .getRequiredAttribute(GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        String requestUri = "";
        for(URI uri:uris){
            uri.getPath();
            if (uri != null
                    && !"lb".equals(uri.getScheme())) {
                requestUri = uri.getPath();
                break;
            }
        }

        return requestUri;
    }
}
