package com.enigma.jobConnector.controller;

import com.enigma.jobConnector.dto.response.CommonResponse;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ErrorControllerTest {

    @Autowired
    private ErrorController errorController;

    @Test
    void shouldHandlingResponseStatusException() {
        String message = "Resource not found";
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        ResponseStatusException exception = new ResponseStatusException(httpStatus, message);

        ResponseEntity<?> response = errorController.handlingResponseStatusException(exception);
        CommonResponse<?> responseBody = (CommonResponse<?>) response.getBody();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(message, responseBody.getMessage());
        assertEquals(null, responseBody.getData());
    }

    @Test
    void shouldHandlingConstraintViolationException() {
        String message = "Field 'email' must not be null.";
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ConstraintViolationException exception = new ConstraintViolationException(message, Collections.emptySet());

        ResponseEntity<?> response = errorController.handlingConstraintViolationException(exception);
        CommonResponse<?> responseBody = (CommonResponse<?>) response.getBody();

        assertEquals(httpStatus, response.getStatusCode());
        assertEquals(message, responseBody.getMessage());
        assertEquals(null, responseBody.getData());
    }

    @Test
    void shouldHandlingDataIntegrityViolationExceptionDuplicatedKeyValue() {
        String causeMessage = "duplicate key value violates unique constraint";
        String responseMessage = "Data already exist.";
        HttpStatus httpStatus = HttpStatus.CONFLICT;

        Throwable cause = new Throwable(causeMessage);
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Error occurred", cause);

        ResponseEntity<?> response = errorController.handlingDataIntegrityViolationException(exception);
        CommonResponse<?> responseBody = (CommonResponse<?>) response.getBody();

        assertEquals(httpStatus, response.getStatusCode());
        assertEquals(responseMessage, responseBody.getMessage());
        assertEquals(null, responseBody.getData());
    }

    @Test
    void shouldHandlingDataIntegrityViolationExceptionDataCantNull() {
        String causeMessage = "Column 'column_name' cannot be null";
        String responseMessage = "Data cannot be null.";
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        Throwable cause = new Throwable(causeMessage);
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Error occurred", cause);

        ResponseEntity<?> response = errorController.handlingDataIntegrityViolationException(exception);
        CommonResponse<?> responseBody = (CommonResponse<?>) response.getBody();

        assertEquals(httpStatus, response.getStatusCode());
        assertEquals(responseMessage, responseBody.getMessage());
        assertEquals(null, responseBody.getData());
    }

    @Test
    void shouldHandlingDataIntegrityViolationExceptionForeignKeyConstraint() {
        String causeMessage = "\"insert or update on table \"table_name\" violates foreign key constraint \"constraint_name\"\n";
        String responseMessage = "Data cannot be deleted because it is used by other data.";
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        Throwable cause = new Throwable(causeMessage);
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Error occurred", cause);

        ResponseEntity<?> response = errorController.handlingDataIntegrityViolationException(exception);
        CommonResponse<?> responseBody = (CommonResponse<?>) response.getBody();

        assertEquals(httpStatus, response.getStatusCode());
        assertEquals(responseMessage, responseBody.getMessage());
        assertEquals(null, responseBody.getData());
    }

    @Test
    void shouldHandlingDataIntegrityViolationExceptionDuplicatedEntry() {
        String causeMessage = "\"Duplicate entry 'value' for key 'key_name'\"";
        String responseMessage = "Data already exist.";
        HttpStatus httpStatus = HttpStatus.CONFLICT;

        Throwable cause = new Throwable(causeMessage);
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Error occurred", cause);

        ResponseEntity<?> response = errorController.handlingDataIntegrityViolationException(exception);
        CommonResponse<?> responseBody = (CommonResponse<?>) response.getBody();

        assertEquals(httpStatus, response.getStatusCode());
        assertEquals(responseMessage, responseBody.getMessage());
        assertEquals(null, responseBody.getData());
    }

    @Test
    void shouldHandlingDataIntegrityViolationExceptionOthersException() {
        String causeMessage = "\"ERROR: relation \\\"table_name\\\" does not exist\"";
        String responseMessage = "Unexpected error occurred";
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        Throwable cause = new Throwable(causeMessage);
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Error occurred", cause);

        ResponseEntity<?> response = errorController.handlingDataIntegrityViolationException(exception);
        CommonResponse<?> responseBody = (CommonResponse<?>) response.getBody();

        assertEquals(httpStatus, response.getStatusCode());
        assertEquals(responseMessage, responseBody.getMessage());
        assertEquals(null, responseBody.getData());
    }
}

