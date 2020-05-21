package org.tieland.melon.admin.common;

/**
 * 定义全局返回enum
 * @author zhouxiang
 * @date 2018/6/20 8:59
 */
public enum GlobalCodeEnum {

    /** 操作成功 */
    SUCCESS(1, "操作成功"),

    /** 系统异常 */
    ERROR(-1, "系统异常"),

    /**
     * 参数不正确
     */
    PARAMTER_ERROR(000000001, "参数不正确");

    GlobalCodeEnum(final Integer code, final String message){
        this.code = code;
        this.message = message;
    }

    private Integer code;

    private String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
