package org.tieland.melon.core;

/**
 * Melon配置服务
 * @author zhouxiang
 * @date 2019/8/27 14:15
 */
public interface MelonService {

    /**
     * 全局Melon配置
     * @return
     */
    MelonSettings getSettings();

}
