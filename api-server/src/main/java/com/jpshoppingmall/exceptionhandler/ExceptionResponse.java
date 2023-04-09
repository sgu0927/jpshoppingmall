package com.jpshoppingmall.exceptionhandler;

import com.jpshoppingmall.exception.CommonException;
import com.jpshoppingmall.exception.CommonExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ExceptionResponse {
    private final String errorCode;
    private final String errorMessage;

    public ExceptionResponse(CommonExceptionType type) {
        this.errorCode = type.getCode();
        this.errorMessage = type.getMessage();
    }

    public ExceptionResponse(CommonExceptionType type, Exception e) {
        this.errorCode = type.getCode();
        this.errorMessage = type.getMessage() + ", " + e.getMessage();
    }

    public ExceptionResponse(CommonException e) {
        this.errorCode = e.getType().getCode();
        this.errorMessage = e.getMessage();
    }
}
