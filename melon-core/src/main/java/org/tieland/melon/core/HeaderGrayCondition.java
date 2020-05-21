package org.tieland.melon.core;

import lombok.Data;

import java.io.Serializable;

/**
 * header条件值
 * @author zhouxiang
 * @date 2019/8/28 8:57
 */
@Data
public class HeaderGrayCondition implements GrayCondition, Serializable {

    /**
     * header key
     */
    private String key;

    /**
     * header value
     */
    private String value;

}
