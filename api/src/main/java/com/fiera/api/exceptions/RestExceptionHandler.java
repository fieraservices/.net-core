package com.fiera.api.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.persistence.RollbackException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 
     * @param e
     * @return
     */
    @ExceptionHandler(TransactionSystemException.class)
    protected ResponseEntity<List<String>> handleTransactionException(TransactionSystemException ex) throws Throwable {
        Throwable cause = ex.getCause();
        if (!(cause instanceof RollbackException))
            throw cause;
        if (!(cause.getCause() instanceof ConstraintViolationException))
            throw cause.getCause();
        ConstraintViolationException validationException = (ConstraintViolationException) cause.getCause();
        List<String> messages = validationException.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage).collect(Collectors.toList());
        return new ResponseEntity<>(messages, HttpStatus.BAD_REQUEST);
    }

    /**
     * 
     * @param e
     * @return
     */
    @ExceptionHandler({ HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class })
    public ResponseEntity<?> handleException(NestedRuntimeException e) {
        String message = "Error parsing payload, check json syntax, date format and data types.";
        ErrorMessage error = ErrorMessage.of(message, e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * 
     * @param e
     * @return
     */
    @ExceptionHandler({ IOException.class })
    public ResponseEntity<?> handleException(IOException e) {
        String message = "Unexpected error occurred, please contact support.";
        ErrorMessage error = ErrorMessage.of(message, e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 
     * @param e
     * @return
     */
    @ExceptionHandler({ RestClientException.class })
    public ResponseEntity<?> handleException(RestClientException e) {
        String message = "Unexpected error occurred while calling external resource(s), please contact support.";
        ErrorMessage error = ErrorMessage.of(message, e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 
     * @param e
     * @return
     */
    @ExceptionHandler({ InstantiationException.class, IllegalAccessException.class })
    public ResponseEntity<?> handleException(ReflectiveOperationException e) {
        String message = "Unexpected error occurred, please contact support.";
        ErrorMessage error = ErrorMessage.of(message, e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 
     * @param e
     * @return
     */
    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<?> recordNotFoundException(RecordNotFoundException e) {
        String message = "Record not found.";
        ErrorMessage error = ErrorMessage.of(message, e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}