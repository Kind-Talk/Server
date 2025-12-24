package com.kindtalk.server.member.dto;

import com.kindtalk.server.member.domain.Member;
import com.kindtalk.server.member.role.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;

public record MemberJoinRequest(
    @NotBlank
    String email,

    @NotBlank
    String password,

    @NotBlank
    String userName,

    @NotBlank
    String nickName,

    @NotNull
    Role role) {

    public Member toEntity(PasswordEncoder passwordEncoder) {
        return new Member(
            this.email,
            passwordEncoder.encode(this.password),
            this.userName,
            this.nickName,
            this.role
        );
    }
}
