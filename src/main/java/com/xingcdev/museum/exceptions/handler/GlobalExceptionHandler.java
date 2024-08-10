package com.xingcdev.museum.exceptions.handler;

import com.xingcdev.museum.domain.dto.DetailDto;
import com.xingcdev.museum.domain.dto.ErrorDto;
import com.xingcdev.museum.exceptions.InvalidSortingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;

@ControllerAdvice
public class GlobalExceptionHandler {

    // ====== Spring Boot built-in exceptions ======
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException() {
        var errorDTO = ErrorDto.builder()
                .code("unknownError")
                .message("The server has encountered an unknown error. Please try again.")
                .build();
        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDto> handleTypeMismatch() {
        var errorDTO = ErrorDto.builder()
                .code("typeMismatch")
                .message("The id you entered is invalid. Please try again.")
                .build();
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleHttpMessageNotReadable() {
        var errorDTO = ErrorDto.builder()
                .code("httpMessageNotReadable")
                .message("The request body seems incorrect. Please provide a valid body.")
                .build();
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorDto> handleNoResourceFound() {
        var errorDTO = ErrorDto.builder()
                .code("noResourceFound")
                .message("The requested resource is not found. Please provide a valid path.")
                .build();
        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        var detailDtos = new ArrayList<DetailDto>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName;
            try {
                fieldName = ((FieldError) error).getField();

            } catch (ClassCastException classCastException) {
                fieldName = error.getObjectName();
            }
            var detailDto = new DetailDto(fieldName, error.getDefaultMessage());
            detailDtos.add(detailDto);
        });

        var errorDTO = ErrorDto.builder()
                .code("validationFailed")
                .message("Please provide a valid request body.")
                .details(detailDtos)
                .build();
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    // ====== User-defined exceptions ======

    @ExceptionHandler(InvalidSortingException.class)
    public ResponseEntity<ErrorDto> handleInvalidSorting(InvalidSortingException ex) {
        var errorDTO = ErrorDto.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }
}
