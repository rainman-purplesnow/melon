package org.tieland.melon.core;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认Melon配置，兜底配置
 * @author zhouxiang
 * @date 2019/8/29 9:28
 */
@Slf4j
public class DefaultMelonService implements MelonService {

    public DefaultMelonService(){
        log.warn(" no custom melon service assign. " +
                "default melon service will be init. ");
    }

    @Override
    public MelonSettings getSettings() {
        log.error(" default melon service and melon settings is null. ");
        return null;
    }

}
