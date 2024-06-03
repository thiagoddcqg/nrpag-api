package com.nextreleaseproblem.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ApiErro {

    private int status;
    private String message;
    private long timestamp = System.currentTimeMillis();

    public ApiErro(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

}
