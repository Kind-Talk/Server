package com.kindtalk.server.member.dto;

import jakarta.validation.constraints.NotBlank;

public record MemberUpdateRequest(
    @NotBlank
    String userName,

    @NotBlank
    String nickName) {
}
