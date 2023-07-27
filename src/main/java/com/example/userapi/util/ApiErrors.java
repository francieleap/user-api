package com.example.userapi.util;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class ApiErrors {
    private final List<String> errors;

    public ApiErrors(List<String> errors) {
        this.errors = errors;
    }

    public ApiErrors(String ex) {
        this.errors = Collections.singletonList(ex);
    }

}
