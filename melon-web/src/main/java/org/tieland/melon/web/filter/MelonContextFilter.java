package org.tieland.melon.web.filter;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.GenericFilterBean;
import org.tieland.melon.core.*;
import org.tieland.melon.ribbon.HystrixMelonContextMeshHolder;
import org.tieland.melon.ribbon.MelonContextMesh;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author zhouxiang
 * @date 2019/8/27 10:55
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MelonContextFilter extends GenericFilterBean {

    private MelonConfig melonConfig;

    public MelonContextFilter(MelonConfig melonConfig){
        this.melonConfig = melonConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)servletRequest;
        String melonContextHeader = httpRequest.getHeader(MelonConstants.MS_HEADER_MELON_CONTEXT);
        log.debug(" melon context header:{} ", melonContextHeader);

        MelonContext melonContext;
        if(StringUtils.isBlank(melonContextHeader)){
            log.debug(" this request is not from gateway origin. ");
            melonContext = MelonContextFactory.buildWithLocal();
        }else{
            log.debug(" build melon context from header. ");
            melonContext = MelonContextFactory.buildWithHeader(melonContextHeader);
        }

        if(!HystrixRequestContext.isCurrentThreadInitialized()){
            HystrixRequestContext.initializeContext();
        }

        try{
            MelonInstance melonInstance = MelonInstanceFactory.get(melonConfig, Boolean.FALSE);
            log.debug(" melonContext:{}, melonInstance:{} ", melonContext, melonInstance);
            HystrixMelonContextMeshHolder.set(new MelonContextMesh(melonContext, melonInstance));
            filterChain.doFilter(servletRequest, servletResponse);
        }finally {
            if(HystrixRequestContext.getContextForCurrentThread() != null){
                HystrixRequestContext.getContextForCurrentThread().shutdown();
            }
        }
    }

}
