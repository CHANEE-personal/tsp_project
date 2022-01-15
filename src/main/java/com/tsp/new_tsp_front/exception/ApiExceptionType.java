package com.tsp.new_tsp_front.exception;

import lombok.Getter;

@Getter
public enum ApiExceptionType implements BaseExceptionType {

	// Model 관련 Type
	ERROR_MODEL("ERROR_MODEL", 500, "모델 등록 에러"),
	NOT_FOUND_MODEL("NOT_FOUND_MODEL", 200, "해당 모델 없음"),
	NOT_FOUND_MODEL_LIST("NOT_FOUND_MODEL_LIST", 200, "모델 리스트 없음"),
	// Production 관련 Type
	ERROR_PRODUCTION("ERROR_PRODUCTION", 500, "프로덕션 등록 에러"),
	NOT_FOUND_PRODUCTION("NOT_FOUND_PRODUCTION", 200, "해당 프로덕션 없음"),
	NOT_FOUND_PRODUCTION_LIST("NOT_FOUND_PRODUCTION_LIST", 200, "프로덕션 리스트 없음"),
	// Portfolio 관련 Type
	ERROR_PORTFOLIO("ERROR_PORTFOLIO", 500, "포트폴리오 등록 에러"),
	NOT_FOUND_PORTFOLIO("NOT_FOUND_PORTFOLIO", 200, "해당 포트폴리오 없음"),
	NOT_FOUND_PORTFOLIO_LIST("NOT_FOUND_PORTFOLIO_LIST", 200, "포트폴리오 리스트 없음"),
	// Support 관련 Type
	NOT_FOUND_SUPPORT("NOT_FOUND_SUPPORT", 200, "해당 포트폴리오 없음"),
	NOT_FOUND_SUPPORT_LIST("NOT_FOUND_SUPPORT_LIST", 200, "포트폴리오 리스트 없음"),
	// 서버 관련 TYPE
	RUNTIME_EXCEPTION("SERVER_ERROR", 500, "서버에러"),
	BAD_REQUEST("", 401, "권한에러"),
	NOT_NULL("NOT_NULL", 400, "필수값 누락"),
	ID_EXIST("ID_EXIST", 400, "같은 아이디 존재"),
	// 이미지 관련 TYPE
	NOT_EXIST_IMAGE("ERROR_IMAGE", 500, "이미지 등록 에러"),
	// 공통 코드 관련 TYPE
	ERROR_COMMON("ERROR_COMMON", 500, "공통 코드 등록 에러"),
	NOT_FOUND_COMMON("NOT_FOUND_COMMON", 200, "해당 공통코드 없음"),
	NOT_FOUND_COMMON_LIST("NOT_FOUND_COMMON_LIST", 200, "공통 코드 리스트 없음");

	private final String errorCode;
	private final int httpStatus;
	private final String errorMessage;

	ApiExceptionType(String errorCode, int httpStatus, String errorMessage) {
		this.errorCode = errorCode;
		this.httpStatus = httpStatus;
		this.errorMessage = errorMessage;
	}

}
