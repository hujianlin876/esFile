package com.esfile.common.exception;
/**
 * 参数验证异常
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
