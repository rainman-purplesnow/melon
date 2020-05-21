package org.tieland.melon.core;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhouxiang
 * @date 2020/3/17 15:33
 */
public final class GrayRuleBus {

    private static final Map<String, GrayRule> RULE_MAPPINGS = new ConcurrentHashMap<>();

    static{
        ServiceLoader<GrayRule> serviceLoader = ServiceLoader.load(GrayRule.class);
        if(serviceLoader == null){
            throw new NoGrayRuleException(" no gray rule spi found ");
        }

        Iterator<GrayRule> iterator = serviceLoader.iterator();
        List<GrayRule> rules = IteratorUtils.toList(iterator);
        if(CollectionUtils.isEmpty(rules)){
            throw new NoGrayRuleException("  no gray rule found ");
        }

        rules.forEach(rule->{
            if(StringUtils.isBlank(rule.name())){
                throw new RuntimeException();
            }

            RULE_MAPPINGS.put(rule.name(), rule);
        });
    }

    private GrayRuleBus(){
        //
    }

    /**
     * 获取对应灰度规则
     * @param key
     * @return
     */
    public static GrayRule get(String key){
        return RULE_MAPPINGS.get(key);
    }

}
