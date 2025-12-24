package com.kindtalk.server.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindtalk.server.config.security.MyUserDetails;
import com.kindtalk.server.config.security.dto.LoginResponse;
import com.kindtalk.server.member.domain.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.OK.value());

        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        Member member = myUserDetails.getMember();

        LoginResponse data = new LoginResponse(member.getEmail(), member.getRole());
        response.getWriter().write(objectMapper.writeValueAsString(data));
    }
}
