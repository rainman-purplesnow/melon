package org.tieland.melon.core;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.Set;

/**
 * @author zhouxiang
 * @date 2020/3/17 11:00
 */
@ToString
@EqualsAndHashCode
public final class MelonContext {

    /**
     * 访问源头
     */
    private AccessOrigin accessOrigin;

    /**
     * 灰度是否被激活
     */
    private boolean grayActivated;

    /**
     * 白名单组-可备选
     */
    private Set<String> whiteGroups;

    /**
     * 黑名单组-禁用组
     */
    private Set<String> blackGroups;

    private MelonContext(final AccessOrigin accessOrigin, final Boolean grayActivated,
                         final Set<String> whiteGroups, final Set<String> blackGroups){
        this.accessOrigin = accessOrigin;
        this.grayActivated = grayActivated;
        this.whiteGroups = whiteGroups;
        this.blackGroups = blackGroups;
    }

    public AccessOrigin getAccessOrigin() {
        return accessOrigin;
    }

    public boolean isGrayActivated() {
        return grayActivated;
    }

    public Set<String> getWhiteGroups() {
        return whiteGroups;
    }

    public Set<String> getBlackGroups() {
        return blackGroups;
    }

    /**
     * Builder
     */
    public static class Builder {

        private AccessOrigin accessOrigin;

        private boolean grayActivated;

        private Set<String> whiteGroups;

        private Set<String> blackGroups;

        public Builder accessOrigin(AccessOrigin accessOrigin){
            this.accessOrigin = accessOrigin;
            return this;
        }

        public Builder grayActivated(boolean grayActivated){
            this.grayActivated = grayActivated;
            return this;
        }

        public Builder whiteGroups(Set<String> whiteGroups){
            this.whiteGroups = whiteGroups;
            return this;
        }

        public Builder blackGroups(Set<String> blackGroups){
            this.blackGroups = blackGroups;
            return this;
        }

        public MelonContext build(){
            if(accessOrigin == null){
                throw new MelonException(" origin must not be null. ");
            }

            return new MelonContext(this.accessOrigin, this.grayActivated, this.whiteGroups, this.blackGroups);
        }
    }
}
