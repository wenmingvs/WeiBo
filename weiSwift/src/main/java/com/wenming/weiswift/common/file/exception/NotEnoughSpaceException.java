package com.wenming.weiswift.common.file.exception;

/**
 * func: 没有足够空间会抛出这个异常
 */
public class NotEnoughSpaceException extends Exception {
    public NotEnoughSpaceException() {
        super();
    }

    public NotEnoughSpaceException(String detailMessage) {
        super(detailMessage);
    }
}
