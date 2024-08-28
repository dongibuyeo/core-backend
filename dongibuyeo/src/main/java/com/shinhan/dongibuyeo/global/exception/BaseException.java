package com.shinhan.dongibuyeo.global.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class BaseException extends RuntimeException {
    private final String code;
    private Map<String, String> errors = new HashMap<>();

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BaseException(String code, String message, Map<String, String> errors) {
        super(message);
        this.code = code;
        this.errors = errors;
    }

}