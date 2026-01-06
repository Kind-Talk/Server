package com.kindtalk.server.exception;

public class DataAlreadyExistsException extends RuntimeException {

  public DataAlreadyExistsException(String message) {
    super(message);
  }
}
