package com.kindtalk.server.config;

import com.kindtalk.server.security.handler.CustomLoginFailureHandler;
import com.kindtalk.server.security.handler.CustomLoginSuccessHandler;
import com.kindtalk.server.security.handler.CustomLogoutSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityFilterConfig {

  private final CustomLoginSuccessHandler customLoginSuccessHandler;
  private final CustomLoginFailureHandler customLoginFailureHandler;
  private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf(AbstractHttpConfigurer::disable)

      .headers(headers -> headers
        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
      )
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/h2-console/**").permitAll()
        .requestMatchers("/api/school", "/api/school/update").permitAll()
        .requestMatchers("/api/member/join", "/api/member/login").permitAll()
        .requestMatchers("/error").permitAll()
        .requestMatchers("/api/member/me").authenticated()
        .anyRequest().authenticated()
      )
      .formLogin(login -> login
        .loginProcessingUrl("/api/member/login")
        .successHandler(customLoginSuccessHandler)
        .failureHandler(customLoginFailureHandler)
      )
      .logout(logout -> logout
        .logoutUrl("/api/member/logout")
        .logoutSuccessHandler(customLogoutSuccessHandler)
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID")
      );

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
