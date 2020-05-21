package org.tieland.melon.zuul.support;

import com.netflix.zuul.context.RequestContext;
import org.tieland.melon.core.GrayRequestContext;

/**
 * 上下文
 * @author zhouxiang
 * @date 2019/8/28 10:08
 */
public class ZuulGrayRequestContext implements GrayRequestContext {

    private RequestContext requestContext;

    public ZuulGrayRequestContext(RequestContext requestContext){
        this.requestContext = requestContext;
    }

    public RequestContext getRequestContext() {
        return requestContext;
    }
}
