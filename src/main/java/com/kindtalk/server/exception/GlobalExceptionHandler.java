package com.kindtalk.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

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

  @ExceptionHandler(value = BadRequestException.class)
  public ResponseEntity<ErrorResponse> handleBadRequestException(
    BadRequestException e) {
    return new ResponseEntity<>(
      new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage()),
      HttpStatus.BAD_REQUEST
    );
  }

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public ResponseEntity<ValidationResponse> handleMethodArgumentNotValidException(
    MethodArgumentNotValidException e) {
    List<String> errors = e.getAllErrors().stream().map(ObjectError::getDefaultMessage).toList();
    return new ResponseEntity<>(
      new ValidationResponse(HttpStatus.BAD_REQUEST, errors),
      HttpStatus.BAD_REQUEST
    );
  }
}
