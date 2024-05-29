package com.yu.api;

/**
 * @author 余卫青
 * @version 1.0.0
 * @date 2024/4/25 16:54
 **/
public class AliIotException extends RuntimeException {
    public AliIotException(String message) {
        super("阿里物联网接口失败：" + message);
    }

    public AliIotException(Throwable cause) {
        super(cause);
    }
}
