package org.tieland.melon.core;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 请求路径条件值
 * @author zhouxiang
 * @date 2019/8/28 9:06
 */
@Data
public class PathGrayCondition implements GrayCondition, Serializable {

    /**
     * uri
     */
    private String uri;

    /**
     * method
     */
    private String method;

    /**
     * 参数
     */
    private Map<String, String> values;

}
