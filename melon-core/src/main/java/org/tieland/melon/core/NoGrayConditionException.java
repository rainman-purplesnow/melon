package org.tieland.melon.core;

/**
 * @author zhouxiang
 * @date 2020/3/17 16:20
 */
public class NoGrayConditionException extends RuntimeException {

    public NoGrayConditionException(String message){
        super(message);
    }

    public NoGrayConditionException(Throwable throwable){
        super(throwable);
    }

    public NoGrayConditionException(String message, Throwable throwable){
        super(message, throwable);
    }

}
