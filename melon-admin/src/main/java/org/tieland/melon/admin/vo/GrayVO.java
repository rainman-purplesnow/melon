package org.tieland.melon.admin.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import java.io.Serializable;

/**
 * @author zhouxiang
 * @date 2019/8/28 15:13
 */
@Data
public class GrayVO implements Serializable {

    private Long id;

    private Long melonId;

    private String grayNo;

    private String rule;

    private String[] groups;

    private JSONObject json;

    private String conditionClass;

}
