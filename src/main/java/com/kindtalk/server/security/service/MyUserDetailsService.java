package com.kindtalk.server.security.service;

import com.kindtalk.server.member.domain.Member;
import com.kindtalk.server.member.repository.MemberRepository;
import com.kindtalk.server.security.principal.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Member member = memberRepository.findByEmail(username)
      .orElseThrow(() -> new UsernameNotFoundException("회원이 존재하지 않습니다."));

    return new MyUserDetails(member);
  }
}
