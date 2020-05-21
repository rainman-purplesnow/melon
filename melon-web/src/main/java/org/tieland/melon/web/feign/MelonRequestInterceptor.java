package org.tieland.melon.web.feign;

import com.alibaba.fastjson.JSONObject;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.tieland.melon.core.MelonConstants;
import org.tieland.melon.core.MelonException;
import org.tieland.melon.ribbon.HystrixMelonContextMeshHolder;
import org.tieland.melon.ribbon.MelonContextMesh;

/**
 * @author zhouxiang
 * @date 2020/3/17 16:35
 */
@Slf4j
public class MelonRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        MelonContextMesh contextMesh = HystrixMelonContextMeshHolder.get();
        if(contextMesh == null){
            throw new MelonException(" melon context is not exist. ");
        }
        String melonContextHeader = JSONObject.toJSONString(contextMesh.getContext());
        log.debug(" feign request interceptor melon context header:{} ", melonContextHeader);
        requestTemplate.header(MelonConstants.MS_HEADER_MELON_CONTEXT, melonContextHeader);
    }
}
