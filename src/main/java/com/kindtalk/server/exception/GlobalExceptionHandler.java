package com.kindtalk.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

  @ExceptionHandler(value = BadGatewayException.class)
  public ResponseEntity<ErrorResponse> handleBadGatewayException(
    BadGatewayException e) {
    return new ResponseEntity<>(
      new ErrorResponse(HttpStatus.BAD_GATEWAY, e.getMessage()),
      HttpStatus.BAD_GATEWAY
    );
  }

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
    MethodArgumentNotValidException e) {
    String message = e.getAllErrors().get(0).getDefaultMessage();
    return new ResponseEntity<>(
      new ErrorResponse(HttpStatus.BAD_REQUEST, message),
      HttpStatus.BAD_REQUEST
    );
  }
}
