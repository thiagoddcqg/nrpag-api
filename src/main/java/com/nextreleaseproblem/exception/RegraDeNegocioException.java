package com.nextreleaseproblem.exception;

import org.springframework.http.HttpStatus;

public class RegraDeNegocioException extends RuntimeException {
    public RegraDeNegocioException(String message) {
        super(message);
    }
}
