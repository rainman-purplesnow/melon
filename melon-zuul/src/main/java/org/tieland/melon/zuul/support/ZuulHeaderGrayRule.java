package org.tieland.melon.zuul.support;

import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.tieland.melon.core.AbstractGrayRule;
import org.tieland.melon.core.HeaderGrayCondition;
import javax.servlet.http.HttpServletRequest;

/**
 * 基于请求header的灰度rule
 * @author zhouxiang
 * @date 2019/8/28 10:16
 */
@Slf4j
public class ZuulHeaderGrayRule extends AbstractGrayRule<ZuulGrayRequestContext, HeaderGrayCondition> {

    private static final String ZUUL_HEADER_GRAY_RULE = "header-gray-rule";

    @Override
    public String name() {
        return ZUUL_HEADER_GRAY_RULE;
    }

    @Override
    protected boolean doApply(ZuulGrayRequestContext zuulGrayRequestContext, HeaderGrayCondition headerGrayCondition) {
        RequestContext requestContext = zuulGrayRequestContext.getRequestContext();
        HttpServletRequest request = requestContext.getRequest();
        String value = request.getHeader(headerGrayCondition.getKey());
        log.debug(" condition header key:{} value:{},  request value:{} ",
                headerGrayCondition.getKey(), headerGrayCondition.getValue(), value);
        if(StringUtils.isNotBlank(value) && headerGrayCondition.getValue().equals(value)){
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }
}
