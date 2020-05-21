package org.tieland.melon.core;

/**
 * @author zhouxiang
 * @date 2019/8/28 10:49
 */
public class DefaultGrayRule implements GrayRule {

    private static final String DEFAULT_GRAY_RULE = "default";

    @Override
    public String name() {
        return DEFAULT_GRAY_RULE;
    }

    @Override
    public boolean apply(GrayRequestContext context, GrayCondition condition) {
        return false;
    }
}
