package com.tsp.exception;

public interface BaseExceptionType {
    String getErrorCode();

    int getHttpStatus();

    String getErrorMessage();
}
