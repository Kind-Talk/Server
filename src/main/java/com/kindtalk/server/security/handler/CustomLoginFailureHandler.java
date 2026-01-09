package com.kindtalk.server.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindtalk.server.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void onAuthenticationFailure(HttpServletRequest request,
                                      HttpServletResponse response,
                                      AuthenticationException exception) throws IOException {
    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(HttpStatus.UNAUTHORIZED.value());

    ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED, getErrorMsg(exception));
    response.getWriter().write(objectMapper.writeValueAsString(error));
  }

  private String getErrorMsg(AuthenticationException exception) {
    if (exception instanceof BadCredentialsException) {
      return "이메일 또는 비밀번호가 일치하지 않습니다.";
    }
    return "로그인에 실패했습니다.";
  }
}
