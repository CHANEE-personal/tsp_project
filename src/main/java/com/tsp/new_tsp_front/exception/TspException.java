package com.tsp.new_tsp_front.exception;

import lombok.Getter;

public class TspException extends RuntimeException{

	@Getter
	private final BaseExceptionType baseExceptionType;

	public TspException(BaseExceptionType baseExceptionType, Throwable e) {
		super(baseExceptionType.getErrorMessage(), e);
		this.baseExceptionType = baseExceptionType;
	}
}
