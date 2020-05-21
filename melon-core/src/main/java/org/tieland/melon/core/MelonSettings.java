package org.tieland.melon.core;

import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Set;

/**
 * @author zhouxiang
 * @date 2020/3/17 11:35
 */
@Data
@ToString
public class MelonSettings {

    /**
     * mode
     */
    private MelonMode mode;

    /**
     * 常规组
     */
    private Set<String> primaryGroups;

    /**
     * 禁用组(无流量)
     */
    private Set<String> forbiddenGroups;

    /**
     * 灰度规则list
     */
    private List<GraySettings> graySettingsList;

}