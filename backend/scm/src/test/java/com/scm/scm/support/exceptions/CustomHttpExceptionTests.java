package com.scm.scm.support.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CustomHttpExceptionTests {

    @Test
    public void testCustomHttpException() {
        String message = "Test message";
        int httpStatusCode = 400;
        ExceptionCause exceptionCause = ExceptionCause.USER_ERROR;

        CustomHttpException exception = new CustomHttpException(message, httpStatusCode, exceptionCause);

        assertEquals(message, exception.getMessage());
        assertEquals(httpStatusCode, exception.getHttpStatusCode());
        assertEquals(exceptionCause, exception.getExceptionCause());
    }

    @Test
    public void testEqualsAndHashCode() {
        CustomHttpException exception1 = new CustomHttpException("Test message", 400, ExceptionCause.USER_ERROR);
        CustomHttpException exception2 = new CustomHttpException("Test message", 400, ExceptionCause.USER_ERROR);
        CustomHttpException exception3 = new CustomHttpException("Different message", 500, ExceptionCause.SERVER_ERROR);

        assertEquals(exception1, exception2);
        assertEquals(exception1.hashCode(), exception2.hashCode());

        assertNotEquals(exception1, exception3);
        assertNotEquals(exception1.hashCode(), exception3.hashCode());
    }

}