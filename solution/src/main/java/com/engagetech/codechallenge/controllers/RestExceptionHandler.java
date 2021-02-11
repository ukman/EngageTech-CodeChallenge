package com.engagetech.codechallenge.controllers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Basic handler which handles exception and provides meaningfull error JSON for UI
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * DTO class for single violation message
     */
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ConstraintViolationMessage {
        private String propertyPath;
        private String message;

        public ConstraintViolationMessage(ConstraintViolation cv) {
            this(cv.getPropertyPath().toString(), cv.getMessage());
        }
    }

    /**
     * DTO class for constraint violation exception
     */
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ConstraintViolationsMessage {
        private String type;
        private String message;
        private List<ConstraintViolationMessage> violations;
    }

    /**
     * Handles constraint violation exception
     * @param e exception to be handled
     * @return DTO with full information for frontend UI
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ConstraintViolationsMessage handleContraintViolation(ConstraintViolationException e) {
        return ConstraintViolationsMessage.builder()
                .type(e.getClass().getName())
                .message(e.getConstraintViolations().toString())
                .violations(e.getConstraintViolations().stream().map(ConstraintViolationMessage::new).collect(Collectors.toList()))
                .build();
    }
}
