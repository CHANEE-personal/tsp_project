package com.tsp.exception;

import lombok.Getter;

public class TspException extends RuntimeException {
    @Getter
    private final BaseExceptionType baseExceptionType;

    public TspException(BaseExceptionType baseExceptionType) {
        super(baseExceptionType.getErrorMessage());
        this.baseExceptionType = baseExceptionType;
    }
}
