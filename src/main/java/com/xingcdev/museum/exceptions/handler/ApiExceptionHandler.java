package com.xingcdev.museum.exceptions.handler;

import com.xingcdev.museum.domain.dto.ErrorDto;
import com.xingcdev.museum.exceptions.DuplicateMuseumException;
import com.xingcdev.museum.exceptions.InvalidSortingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Date;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDto> handleTypeMismatch() {
        var errorDTO = ErrorDto.builder()
                .timestamp(new Date().toString())
                .code("typeMismatch")
                .message("The id you entered is invalid. Please try again.")
                .build();
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidSortingException.class)
    public ResponseEntity<ErrorDto> invalidSortingException(InvalidSortingException ex) {
        var errorDTO = ErrorDto.builder()
                .timestamp(new Date().toString())
                .code(ex.getCode())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }
}
