package com.xingcdev.museum.exceptions.handler;

import com.xingcdev.museum.domain.dto.ErrorDto;
import com.xingcdev.museum.exceptions.VisitNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class VisitExceptionHandler {

    @ExceptionHandler(VisitNotFoundException.class)
    public ResponseEntity<ErrorDto> museumNotFoundException(VisitNotFoundException ex) {
        var errorDTO = ErrorDto.builder()
                .timestamp(new Date().toString())
                .code(ex.getCode())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

}
