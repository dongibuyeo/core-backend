package com.shinhan.dongibuyeo.global.exception;

import java.util.Map;

public class ConflictException extends BaseException {
    public ConflictException(String code, String message) {
        super(code, message);
    }

    public ConflictException(String code, String message, Map<String, String> errors) {
        super(code, message, errors);
    }
}