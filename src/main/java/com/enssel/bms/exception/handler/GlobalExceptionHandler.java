package com.enssel.bms.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // 400
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> badRequestHandle(final BadRequestException ex){
        log.warn("error", ex);
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> badRequestHandle(final MethodArgumentNotValidException ex){
        log.warn("error", ex);
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    // 403
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> accessDeniedHandle(final AccessDeniedException ex){
        log.warn("error", ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    // 404
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Object> notFoundHandle(final NoHandlerFoundException ex){
        log.warn("error", ex);
        return ((BodyBuilder)ResponseEntity.notFound()).body(ex.getMessage());
    }

    // else
    @ExceptionHandler({
            Exception.class
    })
    public ResponseEntity<Object> elseHandle(final Exception ex){
        log.warn("error", ex);
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }
}
