package org.tieland.melon.core;

/**
 * @author zhouxiang
 * @date 2020/3/17 11:00
 */
public class MelonException extends RuntimeException {

    public MelonException(String message){
        super(message);
    }

    public MelonException(String message, Throwable cause){
        super(message, cause);
    }

    public MelonException(Throwable cause){
        super(cause);
    }

}
