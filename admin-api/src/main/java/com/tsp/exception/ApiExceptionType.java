package com.tsp.exception;

import lombok.Getter;

@Getter
public enum ApiExceptionType implements BaseExceptionType {
    // 로그인 관련 Type
    NO_LOGIN("NO_LOGIN", 401, "로그인 필요"),
    NO_ADMIN("NO_ADMIN", 403, "권한 없는 사용자"),
    EXIST_USER("EXIST_USER", 200, "동일한 ID 유저 존재"),

    // User 관련 Type
    ERROR_USER("ERROR_USER", 500, "유저 등록 에러"),
    ERROR_UPDATE_USER("ERROR_UPDATE_USER", 500, "유저 수정 에러"),
    ERROR_DELETE_USER("ERROR_DELETE_USER", 500, "유저 삭제 에러"),
    NOT_FOUND_USER("NOT_FOUND_USER", 404, "해당 유저 없음"),
    NOT_FOUND_USER_LIST("NOT_FOUND_USER_LIST", 404, "유저 리스트 없음"),
    // Model 소속사 관련 Type
    ERROR_AGENCY("ERROR_AGENCY", 500, "소속사 등록 에러"),
    ERROR_UPDATE_AGENCY("ERROR_UPDATE_AGENCY", 500, "소속사 수정 에러"),
    ERROR_DELETE_AGENCY("ERROR_DELETE_AGENCY", 500, "소속사 삭제 에러"),
    NOT_FOUND_AGENCY("NOT_FOUND_AGENCY", 404, "해당 소속사 없음"),
    NOT_FOUND_AGENCY_LIST("NOT_FOUND_AGENCY_LIST", 404, "소속사 리스트 없음"),

    // Model 관련 Type
    ERROR_MODEL("ERROR_MODEL", 500, "모델 등록 에러"),
    ERROR_UPDATE_MODEL("ERROR_UPDATE_MODEL", 500, "모델 수정 에러"),
    ERROR_DELETE_MODEL("ERROR_DELETE_MODEL", 500, "모델 삭제 에러"),
    NOT_FOUND_MODEL("NOT_FOUND_MODEL", 404, "해당 모델 없음"),
    NOT_FOUND_MODEL_LIST("NOT_FOUND_MODEL_LIST", 404, "모델 리스트 없음"),

    // Production 관련 Type
    ERROR_PRODUCTION("ERROR_PRODUCTION", 500, "프로덕션 등록 에러"),
    ERROR_UPDATE_PRODUCTION("ERROR_UPDATE_PRODUCTION", 500, "프로덕션 수정 에러"),
    ERROR_DELETE_PRODUCTION("ERROR_DELETE_PRODUCTION", 500, "프로덕션 삭제 에러"),
    NOT_FOUND_PRODUCTION("NOT_FOUND_PRODUCTION", 404, "해당 프로덕션 없음"),
    NOT_FOUND_PRODUCTION_LIST("NOT_FOUND_PRODUCTION_LIST", 404, "프로덕션 리스트 없음"),

    // Portfolio 관련 Type
    ERROR_PORTFOLIO("ERROR_PORTFOLIO", 500, "포트폴리오 등록 에러"),
    ERROR_UPDATE_PORTFOLIO("ERROR_UPDATE_PORTFOLIO", 500, "포트폴리오 수정 에러"),
    ERROR_DELETE_PORTFOLIO("ERROR_DELETE_PORTFOLIO", 500, "포트폴리오 삭제 에러"),
    NOT_FOUND_PORTFOLIO("NOT_FOUND_PORTFOLIO", 404, "해당 포트폴리오 없음"),
    NOT_FOUND_PORTFOLIO_LIST("NOT_FOUND_PORTFOLIO_LIST", 404, "포트폴리오 리스트 없음"),

    // Support 관련 Type
    ERROR_DELETE_SUPPORT("ERROR_DELETE_SUPPORT", 500, "지원모델 삭제 에러"),
    ERROR_SUPPORT("ERROR_SUPPORT", 500, "지원모델 등록 에러"),
    ERROR_UPDATE_SUPPORT("ERROR_UPDATE_SUPPORT", 500, "지원모델 수정 에러"),
    NOT_FOUND_SUPPORT("NOT_FOUND_SUPPORT", 404, "해당 지원서 없음"),
    NOT_FOUND_SUPPORT_LIST("NOT_FOUND_SUPPORT_LIST", 404, "지원서 리스트 없음"),

    // 서버 관련 TYPE
    RUNTIME_EXCEPTION("SERVER_ERROR", 500, "서버에러"),
    BAD_REQUEST("", 401, "권한에러"),
    NOT_NULL("NOT_NULL", 400, "필수값 누락"),
    ID_EXIST("ID_EXIST", 400, "같은 아이디 존재"),

    // 이미지 관련 TYPE
    NOT_FOUND_IMAGE("NOT_FOUND_IMAGE", 404, "이미지 없음"),
    ERROR_IMAGE("ERROR_IMAGE", 500, "이미지 등록 에러"),
    ERROR_UPDATE_IMAGE("ERROR_UPDATE_IMAGE", 500, "이미지 수정 에러"),
    ERROR_DELETE_IMAGE("ERROR_DELETE_IMAGE", 500, "이미지 삭제 에러"),

    // 공통 코드 관련 TYPE
    ERROR_COMMON("ERROR_COMMON", 500, "공통 코드 등록 에러"),
    ERROR_UPDATE_COMMON("ERROR_UPDATE_COMMON", 500, "공통 코드 수정 에러"),
    ERROR_DELETE_COMMON("ERROR_DELETE_COMMON", 500, "공통 코드 삭제 에러"),
    NOT_FOUND_COMMON("NOT_FOUND_COMMON", 404, "해당 공통코드 없음"),
    NOT_FOUND_COMMON_LIST("NOT_FOUND_COMMON_LIST", 404, "공통 코드 리스트 없음"),

    // 지원모델 평가 관련 TYPE
    NOT_FOUND_EVALUATION_LIST("NOT_FOUND_EVALUATION_LIST", 404, "지원모델 평가 리스트 없음"),
    NOT_FOUND_EVALUATION("NOT_FOUND_EVALUATION", 404, "지원모델 평가 없음"),
    ERROR_EVALUATION("ERROR_EVALUATION", 500, "지원모델 평가 작성 에러"),
    ERROR_UPDATE_EVALUATION("ERROR_UPDATE_EVALUATION", 500, "지원모델 평가 수정 에러"),
    ERROR_DELETE_EVALUATION("ERROR_DELETE_EVALUATION", 500, "지원모델 삭제 에러"),

    // 공지사항 관련 TYPE
    NOT_FOUND_NOTICE_LIST("NOT_FOUND_NOTICE_LIST", 404, "공지사항 리스트 없음"),
    NOT_FOUND_NOTICE("NOT_FOUND_NOTICE", 404, "공지사항 없음"),
    ERROR_NOTICE("ERROR_NOTICE", 500, "공지사항 등록 에러"),
    ERROR_UPDATE_NOTICE("ERROR_UPDATE_NOTICE",500, "공지사항 수정 에러"),
    ERROR_DELETE_NOTICE("ERROR_DELETE_NOTICE", 500, "공지사항 삭제 에러"),

    // FAQ 관련 TYPE
    NOT_FOUND_FAQ_LIST("NOT_FOUND_FAQ_LIST", 404, "FAQ 리스트 없음"),
    NOT_FOUND_FAQ("NOT_FOUND_FAQ", 404, "FAQ 없음"),
    ERROR_FAQ("ERROR_FAQ", 500, "FAQ 등록 에러"),
    ERROR_UPDATE_FAQ("ERROR_UPDATE_FAQ",500, "FAQ 수정 에러"),
    ERROR_DELETE_FAQ("ERROR_DELETE_FAQ", 500, "FAQ 삭제 에러"),

    // 코멘트 관련 TYPE
    NOT_FOUND_COMMENT_LIST("NOT_FOUND_COMMENT_LIST", 404, "코멘트 리스트 없음"),
    NOT_FOUND_COMMENT("NOT_FOUND_COMMENT", 404, "코멘트 없음"),
    ERROR_COMMENT("ERROR_COMMENT", 500, "코멘트 등록 에러"),
    ERROR_UPDATE_COMMENT("ERROR_UPDATE_COMMENT", 500, "코멘트 수정 에러"),
    ERROR_DELETE_COMMENT("ERROR_DELETE_COMMENT", 500, "코멘트 삭제 에러"),

    // 모델 스케줄 관련 TYPE
    NOT_FOUND_MODEL_SCHEDULE_LIST("NOT_FOUND_MODEL_SCHEDULE_LIST", 404, "모델 스케줄 리스트 없음"),
    NOT_FOUND_MODEL_SCHEDULE("NOT_FOUND_MODEL_SCHEDULE", 404, "모델 스케줄 없음"),
    ERROR_MODEL_SCHEDULE("ERROR_MODEL_SCHEDULE", 500, "모델 스케줄 등록 에러"),
    ERROR_UPDATE_MODEL_SCHEDULE("ERROR_UPDATE_MODEL_SCHEDULE", 500, "모델 스케줄 수정 에러"),
    ERROR_DELETE_MODEL_SCHEDULE("ERROR_DELETE_MODEL_SCHEDULE", 500, "모델 스케줄 삭제 에러"),

    // 모델 섭외 관련 TYPE
    NOT_FOUND_MODEL_NEGOTIATION_LIST("NOT_FOUND_MODEL_NEGOTIATION_LIST", 404, "모델 섭외 리스트 없음"),
    NOT_FOUND_MODEL_NEGOTIATION("NOT_FOUND_MODEL_NEGOTIATION", 404, "모델 섭외 없음"),
    ERROR_MODEL_NEGOTIATION("ERROR_MODEL_NEGOTIATION", 500, "모델 섭외 등록 에러"),
    ERROR_UPDATE_MODEL_NEGOTIATION("ERROR_UPDATE_MODEL_NEGOTIATION", 500, "모델 섭외 수정 에러"),
    ERROR_DELETE_MODEL_NEGOTIATION("ERROR_DELETE_MODEL_NEGOTIATION", 500, "모델 섭외 삭제 에러"),

    // 추천 검색어 관련 TYPE
    NOT_FOUND_RECOMMEND_LIST("NOT_FOUND_RECOMMEND_LIST", 404, "추천 검색이 리스트 없음"),
    NOT_FOUND_RECOMMEND("NOT_FOUND_RECOMMEND", 404, "추천 검색어 없음"),
    ERROR_RECOMMEND("ERROR_RECOMMEND", 500, "추천 검색어 등록 에러"),
    ERROR_UPDATE_RECOMMEND("ERROR_UPDATE_RECOMMEND", 500, "추천 검색어 수정 에러"),
    ERROR_DELETE_RECOMMEND("ERROR_DELETE_RECOMMEND", 500, "추천 검색어 삭제 에러"),
    NOT_FOUND_FESTIVAL_LIST("NOT_FOUND_FESTIVAL_LIST", 404, "행사 리스트 없음"),
    NOT_FOUND_FESTIVAL("NOT_FOUND_FESTIVAL", 404, "행사 상세 없음"),
    ERROR_FESTIVAL("ERROR_FESTIVAL", 500, "행사 등록 에러"),
    ERROR_UPDATE_FESTIVAL("ERROR_UPDATE_FESTIVAL", 500, "행사 수정 에러"),
    ERROR_DELETE_FESTIVAL("ERROR_DELETE_FESTIVAL", 500, "행사 삭제 에러");

    private final String errorCode;
    private final int httpStatus;
    private final String errorMessage;

    ApiExceptionType(String errorCode, int httpStatus, String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

}
