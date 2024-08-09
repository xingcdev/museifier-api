package com.xingcdev.museum.exceptions.handler;

import com.xingcdev.museum.domain.dto.ErrorDto;
import com.xingcdev.museum.exceptions.DuplicateMuseumException;
import com.xingcdev.museum.exceptions.MuseumNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MuseumExceptionHandler {

    @ExceptionHandler(MuseumNotFoundException.class)
    public ResponseEntity<ErrorDto> museumNotFoundException(MuseumNotFoundException ex) {
        var errorDTO = ErrorDto.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateMuseumException.class)
    public ResponseEntity<ErrorDto> duplicateMuseumException(DuplicateMuseumException ex) {
        var errorDTO = ErrorDto.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }
}
