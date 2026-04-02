package com.kindtalk.server.security.principal;

import com.kindtalk.server.member.domain.Member;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyUserDetails implements UserDetails, Serializable, CredentialsContainer {

  private static final long serialVersionUID = 1L;

  private Long memberId;
  private String email;
  private String password;
  private String role;

  public MyUserDetails(Member member) {
    this.memberId = member.getId();
    this.email = member.getEmail();
    this.password = member.getPassword();
    this.role = member.getRole().name();
  }

  @Override
  public void eraseCredentials() {
    this.password = null;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + role));
  }

  public String getRole() {
    return role;
  }

  public Long getMemberId() {
    return memberId;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
