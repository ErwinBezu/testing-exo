package org.example.exo3.exceptions;

import org.example.exo3.model.dto.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FurnitureNotFoundException.class)
    public ResponseEntity<ExceptionDTO> handlerConflitException(FurnitureNotFoundException ex){
        ExceptionDTO exceptionDTO = new ExceptionDTO(ex.getMessage(), ex.getHttpStatus().value(), ex.getTime());
        return new ResponseEntity<>(exceptionDTO, ex.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDTO> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .collect(Collectors.joining(" | "));

        ExceptionDTO exceptionDTO = new ExceptionDTO(errorMessage, HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionDTO, HttpStatus.BAD_REQUEST);
    }
}
