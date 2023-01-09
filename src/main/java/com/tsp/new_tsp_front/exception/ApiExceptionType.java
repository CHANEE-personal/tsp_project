package com.tsp.new_tsp_front.exception;

import lombok.Getter;

@Getter
public enum ApiExceptionType implements BaseExceptionType {

    // Model 관련 Type
    ERROR_MODEL("ERROR_MODEL", 500, "모델 등록 에러"),
    NOT_FOUND_MODEL("NOT_FOUND_MODEL", 404, "해당 모델 없음"),
    NOT_FOUND_MODEL_LIST("NOT_FOUND_MODEL_LIST", 404, "모델 리스트 없음"),

    // Model 좋아요 관련 Type
    ERROR_MODEL_LIKE("ERROR_MODEL_LIKE", 500, "모델 좋아요 에러"),

    // Production 관련 Type
    ERROR_PRODUCTION("ERROR_PRODUCTION", 500, "프로덕션 등록 에러"),
    NOT_FOUND_PRODUCTION("NOT_FOUND_PRODUCTION", 404, "해당 프로덕션 없음"),
    NOT_FOUND_PRODUCTION_LIST("NOT_FOUND_PRODUCTION_LIST", 404, "프로덕션 리스트 없음"),

    // Portfolio 관련 Type
    ERROR_PORTFOLIO("ERROR_PORTFOLIO", 500, "포트폴리오 등록 에러"),
    NOT_FOUND_PORTFOLIO("NOT_FOUND_PORTFOLIO", 404, "해당 포트폴리오 없음"),
    NOT_FOUND_PORTFOLIO_LIST("NOT_FOUND_PORTFOLIO_LIST", 404, "포트폴리오 리스트 없음"),

    // Support 관련 Type
    ERROR_SUPPORT("ERROR_SUPPORT", 500, "지원모델 등록 에러"),
    NOT_FOUND_SUPPORT("NOT_FOUND_SUPPORT", 404, "해당 지원모델 없음"),
    NOT_FOUND_SUPPORT_LIST("NOT_FOUND_SUPPORT_LIST", 404, "지원모델 리스트 없음"),

    // 서버 관련 TYPE
    RUNTIME_EXCEPTION("SERVER_ERROR", 500, "서버에러"),
    BAD_REQUEST("", 401, "권한에러"),
    NOT_NULL("NOT_NULL", 400, "필수값 누락"),
    ID_EXIST("ID_EXIST", 400, "같은 아이디 존재"),

    // 이미지 관련 TYPE
    NOT_EXIST_IMAGE("ERROR_IMAGE", 500, "이미지 등록 에러"),

    // 공통 코드 관련 TYPE
    ERROR_COMMON("ERROR_COMMON", 500, "공통 코드 등록 에러"),
    NOT_FOUND_COMMON("NOT_FOUND_COMMON", 404, "해당 공통코드 없음"),
    NOT_FOUND_COMMON_LIST("NOT_FOUND_COMMON_LIST", 404, "공통 코드 리스트 없음"),

    // 공지사항 관련 TYPE
    NOT_FOUND_NOTICE_LIST("NOT_FOUND_NOTICE_LIST", 404, "공지사항 리스트 없음"),
    NOT_FOUND_NOTICE("NOT_FOUND_NOTICE", 404, "해당 공지사항 없음"),

    // FAQ 관련 TYPE
    NOT_FOUND_FAQ_LIST("NOT_FOUND_FAQ_LIST", 404, "FAQ 리스트 없음"),
    NOT_FOUND_FAQ("NOT_FOUND_FAQ", 404, "해당 FAQ 없음"),

    // Agency 관련 TYPE
    NOT_FOUND_AGENCY_LIST("NOT_FOUND_AGENCY_LIST", 404, "Agency 리스트 없음"),
    NOT_FOUND_AGENCY("NOT_FOUND_AGENCY", 404, "해당 Agency 없음"),
    // Agency 좋아요 관련 Type
    ERROR_AGENCY_LIKE("ERROR_AGENCY_LIKE", 500, "Agency 좋아요 에러"),

    // 모델 스케줄 관련 TYPE
    NOT_FOUND_MODEL_SCHEDULE_LIST("NOT_FOUND_MODEL_SCHEDULE_LIST", 404, "모델 스케줄 리스트 없음"),

    // 모델 섭외 관련 Type
    ERROR_MODEL_NEGOTIATION("ERROR_MODEL_NEGOTIATION", 500, "모델섭외 등록 에러"),
    ERROR_UPDATE_MODEL_NEGOTIATION("ERROR_UPDATE_MODEL_NEGOTIATION", 500, "모델섭외 수정 에러"),
    ERROR_DELETE_MODEL_NEGOTIATION("ERROR_DELETE_MODEL_NEGOTIATION", 500, "모델섭외 삭제 에러"),
    NOT_FOUND_MODEL_NEGOTIATION("NOT_FOUND_MODEL_NEGOTIATION", 404, "모델섭외 없음"),
    NOT_FOUND_MODEL_NEGOTIATION_LIST("NOT_FOUND_MODEL_NEGOTIATION_LIST", 404, "모델섭외 리스트 없음"),

    // 행사 관련 Type
    NOT_FOUND_FESTIVAL("NOT_FOUND_FESTIVAL", 404, "행사 상세 없음");
    private final String errorCode;
    private final int httpStatus;
    private final String errorMessage;

    ApiExceptionType(String errorCode, int httpStatus, String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

}
