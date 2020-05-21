package org.tieland.melon.zuul.support;

import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.tieland.melon.core.AbstractGrayRule;
import org.tieland.melon.core.PathGrayCondition;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 基于请求路径的灰度rule
 * @author zhouxiang
 * @date 2019/8/28 10:36
 */
@Slf4j
public class ZuulPathGrayRule extends AbstractGrayRule<ZuulGrayRequestContext, PathGrayCondition> {

    private static final String ZUUL_PATH_GRAY_RULE = "path-gray-rule";

    private PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean doApply(ZuulGrayRequestContext zuulGrayRequestContext, PathGrayCondition pathGrayCondition) {
        RequestContext requestContext = zuulGrayRequestContext.getRequestContext();
        HttpServletRequest request = requestContext.getRequest();
        String requestUri = request.getRequestURI();

        log.debug(" request uri:{}, condition uri:{}", request.getRequestURI(), pathGrayCondition.getUri());
        if(!pathMatcher.match(pathGrayCondition.getUri(), requestUri)){
            log.debug(" uri not match ");
            return Boolean.FALSE;
        }

        if(StringUtils.isNotBlank(pathGrayCondition.getMethod())){
            String requestMethod = request.getMethod();
            log.debug("request method:{}; condition method:{}", requestMethod, pathGrayCondition.getMethod());
            if(!pathGrayCondition.getMethod().equalsIgnoreCase(requestMethod)){
                log.debug(" method not match ");
                return Boolean.FALSE;
            }
        }

        if(MapUtils.isNotEmpty(pathGrayCondition.getValues())){
            for(Map.Entry<String, String> entry : pathGrayCondition.getValues().entrySet()){
                String requestValue = request.getParameter(entry.getKey());
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
        return ZUUL_PATH_GRAY_RULE;
    }
}
