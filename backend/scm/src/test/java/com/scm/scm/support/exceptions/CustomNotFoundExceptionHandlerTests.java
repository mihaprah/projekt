package com.scm.scm.support.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}