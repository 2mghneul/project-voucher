package com.fastcampus.projectvoucher.app.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.BiFunction;

@RestControllerAdvice
public class ApiControllerAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ApiControllerAdvice.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse handleIllegalArgumentException(final IllegalArgumentException e) {
//        logger.info(Arrays.toString(e.getStackTrace()));
//        return createErrorResponse(e.getMessage());
        return createErrorResponse.apply(e, Level.INFO);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalStateException.class)
    public ErrorResponse handleIllegalStateException(final IllegalStateException e) {
        return createErrorResponse.apply(e, Level.INFO);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(final Exception e) {
        return createErrorResponse.apply(e, Level.ERROR);
    }

//    private static ErrorResponse createErrorResponse(String message) {
//        return new ErrorResponse(message, LocalDateTime.now(), UUID.randomUUID());
//    }

    private final BiFunction<Exception, Level, ErrorResponse> createErrorResponse = (e, level) -> {
        final ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), LocalDateTime.now(), UUID.randomUUID());

        if (level == Level.ERROR) {
            logger.error("traceId: {}", errorResponse.traceId(), e);
        } else if (level == Level.INFO) {
            logger.info("traceId: {}", errorResponse.traceId(), e);
        }

        return errorResponse;
    };
}
