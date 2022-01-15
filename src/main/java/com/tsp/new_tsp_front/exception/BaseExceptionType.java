package com.tsp.new_tsp_front.exception;

public interface BaseExceptionType {

	String getErrorCode();
	int getHttpStatus();
	String getErrorMessage();
}
