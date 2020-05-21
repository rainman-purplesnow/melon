package org.tieland.melon.supports.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Melon从cache读取相关配置
 * @author zhouxiang
 * @date 2019/8/29 9:41
 */
@Data
@ConfigurationProperties(prefix = "config.melon.cache")
public class MelonCacheConfig {

    /**
     * redis key
     */
    private String key;

    /**
     * 更新job启动延迟时间（秒）
     */
    private Integer initialDelay = 0;

    /**
     * 更新job运行间隔时间（秒）
     */
    private Integer renewInterval = 60;
}
