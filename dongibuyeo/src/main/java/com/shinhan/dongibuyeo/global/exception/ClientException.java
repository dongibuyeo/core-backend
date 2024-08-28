package com.shinhan.dongibuyeo.global.exception;

import java.util.Map;

public class ClientException extends BaseException {
    public ClientException(String code, String message) {
        super(code, message);
    }

    public ClientException(String code, String message, Map<String, String> errors) {
        super(code, message, errors);
    }
}