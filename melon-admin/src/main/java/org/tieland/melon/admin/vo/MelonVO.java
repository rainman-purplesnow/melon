package org.tieland.melon.admin.vo;

import lombok.Data;
import org.tieland.melon.core.MelonMode;
import java.io.Serializable;
import java.util.List;

/**
 * @author zhouxiang
 * @date 2019/8/28 15:12
 */
@Data
public class MelonVO implements Serializable {

    private Long id;

    private MelonMode mode;

    private String[] primaryGroups;

    private String[] forbiddenGroups;

    private List<GrayVO> graySettingsList;

}
