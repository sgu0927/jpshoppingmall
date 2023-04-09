package com.jpshoppingmall.exceptionhandler;

import com.jpshoppingmall.exception.CommonException;
import com.jpshoppingmall.exception.CommonExceptionType;
import java.nio.file.AccessDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        log.error(e.getMessage(), e);
        final ExceptionResponse response = new ExceptionResponse(
            CommonExceptionType.INTERNAL_SERVER_ERROR, e);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage(), e);
        final ExceptionResponse response = new ExceptionResponse(
            CommonExceptionType.INTERNAL_SERVER_ERROR, e);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ExceptionResponse> handleCustomInternalException(
        CommonException e) {
        log.error(e.getMessage(), e);

        return new ResponseEntity<>(new ExceptionResponse(e), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // When @RequestBody (HttpMessageConverter) cannot bind
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException", e);
        final ExceptionResponse response = new ExceptionResponse(
            CommonExceptionType.INTERNAL_SERVER_ERROR, e);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // When @RequestParam cannot bind an enum type
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ExceptionResponse> handleMethodArgumentTypeMismatchException(
        MethodArgumentTypeMismatchException e) {
        log.error("MethodArgumentTypeMismatchException", e);
        final ExceptionResponse response = new ExceptionResponse(
            CommonExceptionType.INTERNAL_SERVER_ERROR, e);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // There is no handler
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<ExceptionResponse> handleNoHandlerFoundException(
        NoHandlerFoundException e) {
        log.error("NoHandlerFoundException", e);
        final ExceptionResponse response = new ExceptionResponse(
            CommonExceptionType.INTERNAL_SERVER_ERROR, e);

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // There is no http method
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ExceptionResponse> handleHttpRequestMethodNotSupportedException(
        HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException", e);
        final ExceptionResponse response = new ExceptionResponse(
            CommonExceptionType.INTERNAL_SERVER_ERROR, e);

        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    // Authentication object doesn't have any authority
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ExceptionResponse> handleAccessDeniedException(
        AccessDeniedException e) {
        log.error("AccessDeniedException", e);
        final ExceptionResponse response = new ExceptionResponse(
            CommonExceptionType.INTERNAL_SERVER_ERROR, e);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
