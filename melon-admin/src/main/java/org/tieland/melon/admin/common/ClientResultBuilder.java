package org.tieland.melon.admin.common;

/**
 * 接口返回Builder
 * @author zhouxiang
 * @date 2018/6/20 8:30
 */
public final class ClientResultBuilder<T> {

    private Integer code;

    private T data;

    private String message = GlobalCodeEnum.SUCCESS.getMessage();

    public ClientResultBuilder<T> success(){
        this.code = GlobalCodeEnum.SUCCESS.getCode();
        return this;
    }

    public ClientResultBuilder<T> success(final T data){
        this.code = GlobalCodeEnum.SUCCESS.getCode();
        this.data = data;
        return this;
    }

    public ClientResultBuilder<T> error(final Integer code, final String message){
        this.code = code;
        this.message = message;
        return this;
    }

    public ClientResultBuilder<T> error(GlobalCodeEnum codeEnum){
        this.code = codeEnum.getCode();
        this.message = codeEnum.getMessage();
        return this;
    }

    public ClientResult<T> build(){
        ClientResult result = new ClientResult();
        result.setCode(code);
        result.setMessage(message);
        if(GlobalCodeEnum.SUCCESS.getCode().equals(code)){
            if(data!=null){
                result.setData(data);
            }
        }else{
            result.setMessage(message);
        }

        return result;
    }

}
