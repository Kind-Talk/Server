package com.kindtalk.server.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ValidationResponse(HttpStatus httpStatus, List<String> messages) {

}
