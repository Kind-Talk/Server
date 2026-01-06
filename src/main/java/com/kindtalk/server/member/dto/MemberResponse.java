package com.kindtalk.server.member.dto;

import com.kindtalk.server.member.domain.Member;
import com.kindtalk.server.member.role.Role;

public record MemberResponse(
    String email,
    String userName,
    String nickName,
    Role role) {

    public static MemberResponse of(Member member) {
        return new MemberResponse(
            member.getEmail(),
            member.getUserName(),
            member.getNickName(),
            member.getRole()
        );
    }
}
