package org.tieland.melon.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhouxiang
 * @date 2020/3/17 10:53
 */
@Data
@ConfigurationProperties(prefix = "config.melon")
public class MelonConfig {

    /**
     * 是否启用
     */
    private boolean enabled = false;

    /**
     * 必须指定组
     */
    private String group;

}
