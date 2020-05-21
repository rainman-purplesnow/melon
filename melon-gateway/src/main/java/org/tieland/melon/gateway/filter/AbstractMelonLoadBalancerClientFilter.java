package org.tieland.melon.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.tieland.melon.core.*;
import org.tieland.melon.gateway.support.GatewayGrayRequestContext;
import org.tieland.melon.ribbon.MelonContextMesh;
import org.tieland.melon.ribbon.ThreadLocalMelonContextMeshHolder;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author zhouxiang
 * @date 2020/3/19 10:44
 */
@Slf4j
public abstract class AbstractMelonLoadBalancerClientFilter extends LoadBalancerClientFilter {

    protected MelonService melonService;

    protected MelonConfig melonConfig;

    public AbstractMelonLoadBalancerClientFilter(LoadBalancerClient loadBalancer, LoadBalancerProperties properties,
                                                 MelonService melonService, MelonConfig melonConfig) {
        super(loadBalancer, properties);
        this.melonConfig = melonConfig;
        this.melonService = melonService;
    }

    @Override
    protected ServiceInstance choose(ServerWebExchange exchange) {

        try{
            MelonContext melonContext = buildMelonContext(exchange);
            MelonInstance melonInstance = MelonInstanceFactory.get(melonConfig, Boolean.TRUE);
            log.debug(" melonContext:{}, melonInstance:{} ", melonContext, melonInstance);

            ThreadLocalMelonContextMeshHolder.set(new MelonContextMesh(melonContext, melonInstance));
            String melonContextHeader = JSONObject.toJSONString(melonContext);
            log.debug(" melonContextHeader:{} ", melonContextHeader);

            ServerHttpRequest request = exchange.getRequest().mutate()
                    .header(MelonConstants.MS_HEADER_MELON_CONTEXT, melonContextHeader).build();

            return super.choose(exchange.mutate().request(request).build());
        }finally {
            ThreadLocalMelonContextMeshHolder.clear();
        }

    }

    /**
     * 构建 Melon上下文
     * @param exchange
     * @return
     */
    protected MelonContext buildMelonContext(ServerWebExchange exchange){
        MelonSettings melonSettings = melonService.getSettings();
        log.debug(" melonSettings:{} ", melonSettings);
        MelonContext.Builder builder = new MelonContext.Builder();
        builder.accessOrigin(AccessOrigin.GATEWAY);

        //不存在Melon配置时，任意选择
        if(melonSettings == null){
            log.debug(" no melon settings exist. ");
            return builder.grayActivated(Boolean.FALSE).build();
        }

        //Melon Mode=NORMAL时，如果有指定常规primaryGroups配置时，流量走相应组
        //当没有指定时，则任意选择
        if(melonSettings.getMode() == MelonMode.NORMAL){
            log.debug(" melon settings exist. mode is normal. ");
            builder.grayActivated(Boolean.FALSE);
            buildWithPrimaryGroups(builder, melonSettings);
            buildWithForbiddenGroups(builder, melonSettings);
            return builder.build();
        }

        //Melon Mode=GRAY，如果有应该灰度规则匹配，则流量走相应组
        //如果没有匹配:
        //   1.流量导向常规组，有primaryGroups配置，则走相应组
        //   2.如果没有primaryGroups配置，则导向排除所有灰度组和黑名单组之后的相应组
        if(melonSettings.getMode() == MelonMode.GRAY){
            log.debug(" melon settings exist. mode is gray. ");
            builder.grayActivated(Boolean.TRUE);
            buildWithGraySettings(builder, melonSettings, exchange);
            return builder.build();
        }

        log.warn(" melonSettings is error. ");
        return null;
    }

    /**
     * 根据常规组primaryGroups配置，选择相应组
     * @param builder
     * @param melonSettings
     */
    private void buildWithPrimaryGroups(MelonContext.Builder builder, MelonSettings melonSettings){
        if(CollectionUtils.isNotEmpty(melonSettings.getPrimaryGroups())){
            log.debug(" primaryGroups:{} ", melonSettings.getPrimaryGroups());
            builder.whiteGroups(melonSettings.getPrimaryGroups());
        }
    }

    /**
     * 过滤forbiddenGroups
     * @param builder
     * @param melonSettings
     */
    private void buildWithForbiddenGroups(MelonContext.Builder builder, MelonSettings melonSettings){
        if(CollectionUtils.isNotEmpty(melonSettings.getForbiddenGroups())){
            log.debug(" forbiddenGroups:{} ", melonSettings.getForbiddenGroups());
            builder.blackGroups(melonSettings.getForbiddenGroups());
        }
    }

    /**
     * 根据灰度规则匹配，选择相应组
     * @param builder
     * @param melonSettings
     */
    private void buildWithGraySettings(MelonContext.Builder builder, MelonSettings melonSettings, ServerWebExchange exchange){
        List<GraySettings> graySettingsList = melonSettings.getGraySettingsList();
        Set<String> blackGroups = new HashSet<>();
        boolean matched = false;
        //判断是否有灰度匹配
        if(CollectionUtils.isNotEmpty(graySettingsList)){
            for(GraySettings graySettings:graySettingsList){
                if(CollectionUtils.isEmpty(graySettings.getGroups())){
                    continue;
                }

                blackGroups.addAll(graySettings.getGroups());

                if(matched){
                    continue;
                }

                GrayRule rule = GrayRuleBus.get(graySettings.getRule());
                if(rule == null){
                    log.warn(" rule:{} is not found. ", graySettings.getRule());
                    continue;
                }

                GatewayGrayRequestContext grayRequestContext = getGatewayGrayRequestContext(exchange);
                GrayCondition grayCondition = null;
                try {
                    grayCondition = (GrayCondition) JSONObject.parseObject(graySettings.getJson(),
                            Class.forName(graySettings.getConditionClass()));
                } catch (ClassNotFoundException e) {
                    log.error(" no GrayCondition class found error. ");
                    throw new NoGrayConditionException(e);
                }

                if(rule.apply(grayRequestContext, grayCondition)){
                    builder.whiteGroups(graySettings.getGroups()).build();
                    blackGroups.removeAll(graySettings.getGroups());
                    matched = true;
                }

            }
        }

        //未匹配灰度
        if(!matched){
            log.debug(" not match any gray settings. it will select primary group.");
            buildWithPrimaryGroups(builder, melonSettings);
        }

        if(CollectionUtils.isNotEmpty(melonSettings.getForbiddenGroups())){
            blackGroups.addAll(melonSettings.getForbiddenGroups());
        }

        if(CollectionUtils.isNotEmpty(blackGroups)){
            builder.blackGroups(blackGroups);
        }

    }

    /**
     * 构建 Gateway GrayRequestContext
     * @param exchange
     * @return
     */
    protected abstract GatewayGrayRequestContext getGatewayGrayRequestContext(ServerWebExchange exchange);

}
