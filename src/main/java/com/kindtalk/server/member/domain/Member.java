package com.kindtalk.server.member.domain;

import com.kindtalk.server.member.role.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String userName;
    private String nickName;

    @Enumerated(EnumType.STRING)
    private Role role;

    public Member(String email, String password, String userName, String nickName, Role role) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.nickName = nickName;
        this.role = role;
    }

    public void updateInfo(String userName, String nickName) {
        this.userName = userName;
        this.nickName = nickName;
    }
}
