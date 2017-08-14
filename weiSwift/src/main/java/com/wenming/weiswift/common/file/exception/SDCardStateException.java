package com.wenming.weiswift.common.file.exception;

/**
 * sdcard状态异常类
 */
public class SDCardStateException extends Exception {
    public SDCardStateException() {
        super();
    }

    public SDCardStateException(String detailMessage) {
        super(detailMessage);
    }
}
