package com.datacrowd.backend.common.error;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // ======= Стандартные Spring-ошибки =======

    @Override
    protected org.springframework.http.ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        String path = getPath(request);

        List<ApiErrorResponse.FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toFieldError)
                .toList();

        ApiErrorResponse body = ApiErrorResponse.withFieldErrors(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation failed",
                path,
                fieldErrors
        );

        log.warn("Validation error at {}: {}", path, ex.getMessage());
        return new org.springframework.http.ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected org.springframework.http.ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        String path = getPath(request);

        ApiErrorResponse body = ApiErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Malformed JSON request",
                path
        );

        log.warn("Malformed JSON at {}: {}", path, ex.getMessage());
        return new org.springframework.http.ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // ======= Наши бизнес-ошибки =======

    @ExceptionHandler(IllegalArgumentException.class)
    public org.springframework.http.ResponseEntity<ApiErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex,
            WebRequest request
    ) {
        String path = getPath(request);

        ApiErrorResponse body = ApiErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                path
        );

        log.warn("Bad request at {}: {}", path, ex.getMessage());
        return new org.springframework.http.ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NoSuchElementException.class, EntityNotFoundException.class})
    public org.springframework.http.ResponseEntity<ApiErrorResponse> handleNotFound(
            RuntimeException ex,
            WebRequest request
    ) {
        String path = getPath(request);

        ApiErrorResponse body = ApiErrorResponse.of(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                path
        );

        log.warn("Not found at {}: {}", path, ex.getMessage());
        return new org.springframework.http.ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SecurityException.class)
    public org.springframework.http.ResponseEntity<ApiErrorResponse> handleSecurity(
            SecurityException ex,
            WebRequest request
    ) {
        String path = getPath(request);

        ApiErrorResponse body = ApiErrorResponse.of(
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                ex.getMessage(),
                path
        );

        log.warn("Forbidden at {}: {}", path, ex.getMessage());
        return new org.springframework.http.ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public org.springframework.http.ResponseEntity<ApiErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex,
            WebRequest request
    ) {
        String path = getPath(request);

        List<ApiErrorResponse.FieldError> fieldErrors = ex.getConstraintViolations().stream()
                .map(cv -> new ApiErrorResponse.FieldError(
                        cv.getPropertyPath().toString(),
                        cv.getMessage()
                ))
                .toList();

        ApiErrorResponse body = ApiErrorResponse.withFieldErrors(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation failed",
                path,
                fieldErrors
        );

        log.warn("Constraint violation at {}: {}", path, ex.getMessage());
        return new org.springframework.http.ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }


    // ======= Catch-all =======

    @ExceptionHandler(Exception.class)
    public org.springframework.http.ResponseEntity<ApiErrorResponse> handleAll(
            Exception ex,
            WebRequest request
    ) {
        String path = getPath(request);

        ApiErrorResponse body = ApiErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Unexpected server error",
                path
        );

        log.error("Unexpected error at {}: ", path, ex);
        return new org.springframework.http.ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ======= helpers =======

    private String getPath(WebRequest request) {
        if (request instanceof ServletWebRequest servletRequest) {
            return servletRequest.getRequest().getRequestURI();
        }
        return "";
    }

    private ApiErrorResponse.FieldError toFieldError(FieldError fe) {
        String message = fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "Invalid value";
        return new ApiErrorResponse.FieldError(fe.getField(), message);
    }
}
