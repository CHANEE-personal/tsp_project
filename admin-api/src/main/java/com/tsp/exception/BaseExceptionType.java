package com.tsp.exception;

public interface BaseExceptionType {
    // errorCode
    String getErrorCode();

    // HttpStatus
    int getHttpStatus();

    // errorMessage
    String getErrorMessage();
}
