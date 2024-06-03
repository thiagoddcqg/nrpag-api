package com.nextreleaseproblem.exception;

import com.nextreleaseproblem.model.ApiErro;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErro> handleGlobalException(Exception ex, WebRequest request) {
        ApiErro apiError = new ApiErro(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ApiErro> handleResourceNotFoundException(RegraDeNegocioException ex, WebRequest request) {
        ApiErro apiError = new ApiErro(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

}