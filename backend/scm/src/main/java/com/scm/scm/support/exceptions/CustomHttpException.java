package com.scm.scm.support.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomHttpException extends RuntimeException {
    private final int httpStatusCode;
    private final ExceptionCause exceptionCause;

    public CustomHttpException(String message, int httpStatusCode, ExceptionCause exceptionCause) {
        super(message);
        this.httpStatusCode = httpStatusCode;
        this.exceptionCause = exceptionCause;
    }
}
