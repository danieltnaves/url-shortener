package com.urlshortener.api.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public void handleBadRequest(BadRequestException e) {
        log.error("m=handleBadRequest, Bad Request, {}", e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UnvailableKeyException.class)
    public void handleUnvailableKeyException(UnvailableKeyException e) {
        log.error("m=handleUnvailableKeyException, Unvailable Key Exception, {}", e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public void handleNotFoundException(NotFoundException e) {
        log.error("m=handleNotFoundException, Not Found Exception, {}", e.getMessage());
    }

}
