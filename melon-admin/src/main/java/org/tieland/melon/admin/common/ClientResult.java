package org.tieland.melon.admin.common;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 接口返回对象
 * <p>
 *     code=1（操作成功）
 *     code=-1（系统异常）
 *     code=xxx xxx xx（错误码）
 * </p>
 * @author zhouxiang
 * @date 2018/6/20 8:27
 */
@Data
@NoArgsConstructor
public class ClientResult<T> implements Serializable {

    private static final long serialVersionUID = 7276088992244197729L;

    /** 返回code */
    private Integer code;

    /** 返回message */
    private String message;

    /** 返回数据 */
    private T data;

}
