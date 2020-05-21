package org.tieland.melon.core;

import lombok.ToString;

/**
 * @author zhouxiang
 * @date 2020/3/17 11:01
 */
@ToString
public final class MelonInstance {

    /**
     * 服务所在组
     */
    private String group;

    /**
     * 当前应用是否网关
     */
    private boolean gateway;

    public MelonInstance(String group, boolean gateway){
        this.group = group;
        this.gateway = gateway;
    }

    public String getGroup() {
        return group;
    }

    public boolean isGateway() {
        return gateway;
    }

}
