package com.xingcdev.museum.exceptions.handler;

import com.xingcdev.museum.domain.dto.ErrorDto;
import com.xingcdev.museum.exceptions.MuseumAlreadyVisitedException;
import com.xingcdev.museum.exceptions.VisitNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class VisitExceptionHandler {

    @ExceptionHandler(VisitNotFoundException.class)
    public ResponseEntity<ErrorDto> museumNotFoundException(VisitNotFoundException ex) {
        var errorDTO = ErrorDto.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MuseumAlreadyVisitedException.class)
    public ResponseEntity<ErrorDto> museumAlreadyVisitedException(MuseumAlreadyVisitedException ex) {
        var errorDTO = ErrorDto.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

}
