package org.tieland.melon.zuul.filter;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.tieland.melon.core.*;
import org.tieland.melon.ribbon.HystrixMelonContextMeshHolder;
import org.tieland.melon.ribbon.MelonContextMesh;
import org.tieland.melon.zuul.common.ZuulConstants;
import org.tieland.melon.zuul.support.ZuulGrayRequestContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author zhouxiang
 * @date 2020/3/17 14:48
 */
@Slf4j
public abstract class AbstractMelonZuulFilter extends MelonZuulFilter {

    private MelonService melonService;

    private MelonConfig melonConfig;

    public AbstractMelonZuulFilter(MelonService melonService, MelonConfig melonConfig){
        this.melonService = melonService;
        this.melonConfig = melonConfig;
    }

    @Override
    public String filterType() {
        return ZuulConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return ZuulConstants.GRAY_ZUUL_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        MelonContext melonContext = buildMelonContext();
        MelonInstance melonInstance = MelonInstanceFactory.get(melonConfig, Boolean.TRUE);
        log.debug(" melonContext:{}, melonInstance:{} ", melonContext, melonInstance);
        HystrixMelonContextMeshHolder.set(new MelonContextMesh(melonContext, melonInstance));
        String melonContextHeader = JSONObject.toJSONString(melonContext);
        log.debug(" melonContextHeader:{} ", melonContextHeader);
        RequestContext.getCurrentContext().addZuulRequestHeader(MelonConstants.MS_HEADER_MELON_CONTEXT, melonContextHeader);
        return null;
    }

    /**
     * 构建Melon上下文
     * @return
     */
    private MelonContext buildMelonContext(){
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
            buildWithGraySettings(builder, melonSettings);
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
    private void buildWithGraySettings(MelonContext.Builder builder, MelonSettings melonSettings) {
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

                ZuulGrayRequestContext grayRequestContext = getZuulGrayRequestContext(RequestContext.getCurrentContext());
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
     * 构建Zuul GrayRequestContext
     * @param requestContext
     * @return
     */
    protected abstract ZuulGrayRequestContext getZuulGrayRequestContext(RequestContext requestContext);
}
