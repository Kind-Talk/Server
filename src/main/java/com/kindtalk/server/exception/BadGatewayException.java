package com.kindtalk.server.exception;

import lombok.Getter;

@Getter
public class BadGatewayException extends RuntimeException {

  public BadGatewayException(String message) {
    super(message);
  }
}
