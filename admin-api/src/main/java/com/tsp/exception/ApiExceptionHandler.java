package com.tsp.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

import static java.util.Locale.KOREA;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    private final MessageSource messageSource;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class Error {
        private String code;
        private int status;
        private String message;

        static Error create(BaseExceptionType exception) {
            return new Error(exception.getErrorCode(), exception.getHttpStatus(), exception.getErrorMessage());
        }
    }

    @ExceptionHandler(value = TspException.class)
    public ResponseEntity<Error> exception(TspException tspException) {
        return new ResponseEntity<>(Error.create(tspException.getBaseExceptionType()), OK);
    }


    /**
     * <pre>
     * 1. MethodName : handleConstraintViolation
     * 2. ClassName  : ApiExceptionHandler.java
     * 3. Comment    : Parameter Validation Check
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 01. 15.
     * </pre>
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException e, WebRequest request) {
        return handleExceptionInternal(e, messageSource.getMessage("modelCategory.Range", new String[]{}, KOREA), new HttpHeaders(), BAD_REQUEST, request);
    }

    /**
     * <pre>
     * 1. MethodName : handleMethodArgumentNotValid
     * 2. ClassName  : ApiExceptionHandler.java
     * 3. Comment    : Entity or DTO Validation Check
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 01. 15.
     * </pre>
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        ApiExceptionType errorCode = ApiExceptionType.NOT_NULL;
        BindingResult bindingResult = ex.getBindingResult();

        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("](은)는 ");
            builder.append(fieldError.getDefaultMessage());
        }
        builder.deleteCharAt(builder.length() - 1);

        final Error response = new Error(errorCode.getErrorCode(), errorCode.getHttpStatus(), builder.toString());
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getHttpStatus()));
    }
}
