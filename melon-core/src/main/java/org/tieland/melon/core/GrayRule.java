package org.tieland.melon.core;

/**
 * 灰度规则
 * @author zhouxiang
 * @date 2019/8/28 8:48
 */
public interface GrayRule {

    /**
     * 灰度规则名称
     * @return
     */
    String name();

    /**
     * 判断是否匹配
     * @param context
     * @param condition
     * @return
     */
    boolean apply(GrayRequestContext context, GrayCondition condition);
}
