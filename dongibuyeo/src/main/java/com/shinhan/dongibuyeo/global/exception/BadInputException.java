package com.shinhan.dongibuyeo.global.exception;

import java.util.Map;

public class BadInputException extends BaseException {
    public BadInputException(String code, String message) {
        super(code, message);
    }

    public BadInputException(String code, String message, Map<String, String> errors) {
        super(code, message, errors);
    }
}