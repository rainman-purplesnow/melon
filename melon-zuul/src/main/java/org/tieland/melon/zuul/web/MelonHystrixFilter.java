package org.tieland.melon.zuul.web;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author zhouxiang
 * @date 2020/3/17 14:42
 */
@Slf4j
public class MelonHystrixFilter extends GenericFilterBean {

    public MelonHystrixFilter(){
        log.debug(" MelonHystrixFilter init ");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if(!HystrixRequestContext.isCurrentThreadInitialized()){
            HystrixRequestContext.initializeContext();
        }

        try{
            filterChain.doFilter(servletRequest, servletResponse);
        }finally {
            if(HystrixRequestContext.getContextForCurrentThread() != null){
                HystrixRequestContext.getContextForCurrentThread().shutdown();
            }
        }
    }
}
