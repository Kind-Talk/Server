package com.kindtalk.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = DataAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleDataAlreadyExistsException(
    DataAlreadyExistsException e) {
    return new ResponseEntity<>(
      new ErrorResponse(HttpStatus.CONFLICT, e.getMessage()),
      HttpStatus.CONFLICT
    );
  }

  @ExceptionHandler(value = DataNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleDataNotFoundException(
    DataNotFoundException e) {
    return new ResponseEntity<>(
      new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage()),
      HttpStatus.NOT_FOUND
    );
  }
}
