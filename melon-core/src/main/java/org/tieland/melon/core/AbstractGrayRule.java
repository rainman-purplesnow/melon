package org.tieland.melon.core;

/**
 * @author zhouxiang
 * @date 2019/8/28 10:10
 */
public abstract class AbstractGrayRule<K extends GrayRequestContext, P extends GrayCondition> implements GrayRule {

    /**
     * 完成类型转换
     * @param context
     * @param condition
     * @return
     */
    @Override
    public final boolean apply(GrayRequestContext context, GrayCondition condition){
        if(context == null || condition == null){
            throw new IllegalArgumentException(" GrayRequestContext or GrayCondition must not null. ");
        }

        K k = (K)context;
        P p = (P)condition;
        return doApply(k, p);
    }

    /**
     * 实际灰度匹配
     * @param k
     * @param p
     * @return
     */
    protected abstract boolean doApply(K k, P p);

}
