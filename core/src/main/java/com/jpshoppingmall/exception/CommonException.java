package com.jpshoppingmall.exception;

import java.io.Serial;
import lombok.Getter;

@Getter
public class CommonException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    private final CommonExceptionType type;

    public CommonException(CommonExceptionType type) {
        super(type.getMessage());
        this.type = type;
    }

    public CommonException(CommonExceptionType type, String message) {
        super(type.getMessage() + message);
        this.type = type;
    }

    public CommonException(CommonExceptionType type, String stringFormat, Object ...strings) {
        super(type.getMessage() + String.format(stringFormat, strings));
        this.type = type;
    }
}
