package com.scm.scm.support.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class CustomNotFoundExceptionHandler {

    @ExceptionHandler(CustomHttpException.class)
    public ResponseEntity<Object> handleNotFoundException(CustomHttpException ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(
                ex.getMessage(),
                ex.getHttpStatusCode(),
                ex.getExceptionCause()
        );
        return new ResponseEntity<>(errorMessage, HttpStatus.valueOf(ex.getHttpStatusCode()));
    }

    @Data
    static class ErrorMessage {
        private String message;
        private int httpStatusCode;
        private ExceptionCause exceptionCause;

        public ErrorMessage(String message, int httpStatusCode, ExceptionCause exceptionCause) {
            this.message = message;
            this.httpStatusCode = httpStatusCode;
            this.exceptionCause = exceptionCause;
        }
    }
}
