package com.employeeinfomanager.common;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private ReturnNo code;

    public BusinessException(ReturnNo code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ReturnNo code) {
        super(code.getMessage());
        this.code = code;
    }
}
