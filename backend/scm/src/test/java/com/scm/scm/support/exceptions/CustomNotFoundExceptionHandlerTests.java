package com.scm.scm.support.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CustomNotFoundExceptionHandlerTests {

    @Test
    public void testHandleNotFoundException() {
        String message = "Test message";
        int httpStatusCode = 400;
        ExceptionCause exceptionCause = ExceptionCause.USER_ERROR;

        CustomHttpException exception = new CustomHttpException(message, httpStatusCode, exceptionCause);
        CustomNotFoundExceptionHandler handler = new CustomNotFoundExceptionHandler();

        ServletWebRequest request = new ServletWebRequest(new MockHttpServletRequest());

        ResponseEntity<Object> responseEntity = handler.handleNotFoundException(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        CustomNotFoundExceptionHandler.ErrorMessage errorMessage = (CustomNotFoundExceptionHandler.ErrorMessage) responseEntity.getBody();

        assert errorMessage != null;
        assertEquals(message, errorMessage.getMessage());
        assertEquals(httpStatusCode, errorMessage.getHttpStatusCode());
        assertEquals(exceptionCause, errorMessage.getExceptionCause());
    }

    @Test
    public void testErrorMessage() {
        String message = "Test message";
        int httpStatusCode = 400;
        ExceptionCause exceptionCause = ExceptionCause.USER_ERROR;

        CustomNotFoundExceptionHandler.ErrorMessage errorMessage = new CustomNotFoundExceptionHandler.ErrorMessage(message, httpStatusCode, exceptionCause);

        assertEquals(message, errorMessage.getMessage());
        assertEquals(httpStatusCode, errorMessage.getHttpStatusCode());
        assertEquals(exceptionCause, errorMessage.getExceptionCause());

        String newMessage = "New test message";
        int newHttpStatusCode = 500;
        ExceptionCause newExceptionCause = ExceptionCause.SERVER_ERROR;

        errorMessage.setMessage(newMessage);
        errorMessage.setHttpStatusCode(newHttpStatusCode);
        errorMessage.setExceptionCause(newExceptionCause);

        assertEquals(newMessage, errorMessage.getMessage());
        assertEquals(newHttpStatusCode, errorMessage.getHttpStatusCode());
        assertEquals(newExceptionCause, errorMessage.getExceptionCause());
    }

    @Test
    void testToString() {
        String message = "Test message";
        int httpStatusCode = 400;
        ExceptionCause exceptionCause = ExceptionCause.USER_ERROR;

        CustomNotFoundExceptionHandler.ErrorMessage errorMessage = new CustomNotFoundExceptionHandler.ErrorMessage(message, httpStatusCode, exceptionCause);

        String expected = "CustomNotFoundExceptionHandler.ErrorMessage(message=Test message, httpStatusCode=400, exceptionCause=USER_ERROR)";
        assertEquals(expected, errorMessage.toString());
    }

    @Test
    void testEqualsAndHashCode() {
        String message = "Test message";
        int httpStatusCode = 400;
        ExceptionCause exceptionCause = ExceptionCause.USER_ERROR;

        CustomNotFoundExceptionHandler.ErrorMessage errorMessage1 = new CustomNotFoundExceptionHandler.ErrorMessage(message, httpStatusCode, exceptionCause);
        CustomNotFoundExceptionHandler.ErrorMessage errorMessage2 = new CustomNotFoundExceptionHandler.ErrorMessage(message, httpStatusCode, exceptionCause);

        assertTrue(errorMessage1.equals(errorMessage2) && errorMessage2.equals(errorMessage1));
        assertEquals(errorMessage1.hashCode(), errorMessage2.hashCode());
    }

    @Test
    void testNotEqualsAndHashCode() {
        String message1 = "Test message 1";
        int httpStatusCode1 = 400;
        ExceptionCause exceptionCause1 = ExceptionCause.USER_ERROR;

        String message2 = "Test message 2";
        int httpStatusCode2 = 500;
        ExceptionCause exceptionCause2 = ExceptionCause.SERVER_ERROR;

        CustomNotFoundExceptionHandler.ErrorMessage errorMessage1 = new CustomNotFoundExceptionHandler.ErrorMessage(message1, httpStatusCode1, exceptionCause1);
        CustomNotFoundExceptionHandler.ErrorMessage errorMessage2 = new CustomNotFoundExceptionHandler.ErrorMessage(message2, httpStatusCode2, exceptionCause2);

        assertFalse(errorMessage1.equals(errorMessage2) || errorMessage2.equals(errorMessage1));
        assertNotEquals(errorMessage1.hashCode(), errorMessage2.hashCode());
    }
}