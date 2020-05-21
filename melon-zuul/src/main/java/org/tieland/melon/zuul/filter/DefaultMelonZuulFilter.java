package org.tieland.melon.zuul.filter;

import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.tieland.melon.core.MelonConfig;
import org.tieland.melon.core.MelonService;
import org.tieland.melon.zuul.support.ZuulGrayRequestContext;

/**
 * @author zhouxiang
 * @date 2020/3/19 11:15
 */
@Slf4j
public final class DefaultMelonZuulFilter extends AbstractMelonZuulFilter {

    public DefaultMelonZuulFilter(MelonService melonService, MelonConfig melonConfig) {
        super(melonService, melonConfig);
    }

    @Override
    protected ZuulGrayRequestContext getZuulGrayRequestContext(RequestContext requestContext) {
        return new ZuulGrayRequestContext(requestContext);
    }
}
