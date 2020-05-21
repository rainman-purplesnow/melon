package org.tieland.melon.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;

/**
 * @author zhouxiang
 * @date 2019/8/27 14:38
 */
@Data
@ToString
@EqualsAndHashCode
public class GraySettings {

    /**
     * 灰度编号
     */
    private String grayNo;

    /**
     * 灰度组
     */
    private Set<String> groups;

    /**
     * 灰度规则
     */
    private String rule;

    /**
     * 灰度条件值json
     */
    private String json;

    /**
     * 灰度条件值具体Class
     */
    private String conditionClass;
}
